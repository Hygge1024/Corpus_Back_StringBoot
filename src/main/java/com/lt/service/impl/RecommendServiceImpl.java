package com.lt.service.impl;

import com.lt.domain.Corpus;
import com.lt.domain.Favorites;
import com.lt.service.CorpusService;
import com.lt.service.FavoritesService;
import com.lt.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xm.similarity.text.CosineSimilarity;
import org.xm.similarity.text.EditDistanceSimilarity;
import org.xm.similarity.text.TextSimilarity;

import java.util.*;
@Slf4j
@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    private FavoritesService favoritesService;
    @Autowired
    private CorpusService corpusService;
    TextSimilarity cosSimilarity = new CosineSimilarity();//余弦相似度
    TextSimilarity editSimilarity = new EditDistanceSimilarity();//编辑距离相似度
    /**
     * 下面的推荐是针对学生用户的
     * @param userid 学生id
     * @return 返回推荐的语料
     */
    @Override
    public List<Corpus> getRecommendByUserID(String userid) {
        int number_tags = 3;
        int number_tag_corpus = 3;

        /**
         * 1.查询当前用户的收藏信息
         */
        int currentPage = 0;
        int pageSize = -1; //设置为-1就能查需所有的了
        List<Favorites> favoritesList = favoritesService.getAllStu(currentPage,pageSize,userid).getRecords(); //这是用户所搜藏的所有语料
        /**
         * 2.获取每个收藏的tagids，通过Map实现
         */
        Map<Integer,Integer> tagidCountMap = new HashMap<>();
        for(Favorites favorites : favoritesList){
            int tagid = favorites.getTagids();
            tagidCountMap.put(tagid,tagidCountMap.getOrDefault(tagid,0)+1);
        }
        // 打印统计结果
        for (Map.Entry<Integer, Integer> entry : tagidCountMap.entrySet()) {
            System.out.println("Tagid: " + entry.getKey() + ", Count: " + entry.getValue());
        }

        /**
         * 3.对tagids进行降序排序
         */
        // 将Map转换为List进行排序（降序）
        List<Map.Entry<Integer,Integer>> entryList = new ArrayList<>(tagidCountMap.entrySet());
        Collections.sort(entryList, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        // 输出降序排序后的前三个元素
        int count = Math.min(number_tags, entryList.size());
        System.out.println("排序后的前三（如果有）tagid为");
        int[] tagids = new int[number_tags];
        for (int i = 0; i < count; i++) {
            Map.Entry<Integer, Integer> entry = entryList.get(i);
            System.out.println("Tagid: " + entry.getKey() + ", Count: " + entry.getValue());
            //转存到int数组中，方便后续的查找
            tagids[i] = entry.getKey();
        }
        /**
         * 4.取前3的tagids，条件查询corpus，获得3个数组，代表着三个类别
         * 输出结果样例如下：
         * Tagid: 3, Count: 3
         * Tagid: 5, Count: 5
         * Tagid: 6, Count: 1
         * Tagid: 9, Count: 1
         * Tagid: 733, Count: 1
         * 排序后的前三（如果有）tagid为
         * Tagid: 5, Count: 5
         * Tagid: 3, Count: 3
         * Tagid: 6, Count: 1
         */
        List<Corpus> allCorpus = new ArrayList<>(); //存放最终的推荐数据
        //为了实现推荐，存储现有收藏的语料favoriteCorpusList
        List<Corpus> favoriteCorpusList = new ArrayList<>();
        for(Favorites favorites : favoritesList){
            favoriteCorpusList.add(corpusService.getOneCorpus(favorites.getCid()));
        }
//        log.info("收藏者收藏的语料："+favoriteCorpusList);
        for(int tagid : tagids){
            List<Corpus> corpusList = corpusService.getByTag_ids(tagid); //通过tagid找到的相似corpus数据（还需要下面通过相关算法实现计算）
            /**
             * 5.将三个数组 通过相似度算法得到最相似的 n 个corpus（具体多少后续讨论）
             */
//            log.info("当前tagid："+tagid+"对应的corpusList："+corpusList+"\n");
            log.info("\n");
            log.info("当前tagid："+tagid+"，对应的corpuSsize："+corpusList.size());

            double[] similarities = new double[corpusList.size()];
            for(int i = 0; i < corpusList.size(); i++){
                Corpus corpus = corpusList.get(i);
                double maxSimilarity = SimilarityCalculator(favoriteCorpusList,corpus);
                similarities[i] = maxSimilarity;
            }

            log.info("当前tagid："+tagid+"，similarities数量为："+similarities.length+"，内容为："+Arrays.toString(similarities));
            //开始根据similarities中number_tag_corpus个最大值选择对应的corpus到allCorpus中
            log.info("开始统计similarities中最大的number_tag_corpus个，对应的corpus");
            for (int j = 0; j < number_tag_corpus; j++) {
                log.info("当前tagid："+tagid+"，正在进行的第"+j+"个");
                double maxSimilarity = Double.MIN_VALUE;
                int maxIndex = -1;
                for (int i = 0; i < similarities.length; i++) {
                    if (similarities[i] > maxSimilarity) {
                        maxSimilarity = similarities[i];
                        maxIndex = i;
                    }
                }
                if (maxIndex != -1) {
                    /**
                     * 将每个类别中符合条件的添加到allCorpus中
                     */
                    log.info("当前maxIndex："+maxIndex+"，当前maxSimilarity："+maxSimilarity);
                    allCorpus.add(corpusList.get(maxIndex));
                    log.info("当前添加的corpus.Title="+corpusList.get(maxIndex).getTitle());
                    // 将已经选择的相似度值置为最小值，确保下次不会再被选择
                    similarities[maxIndex] = Double.MIN_VALUE;
                } else {
                    break; // 如果没有找到更大的相似度值，则跳出循环
                }
            }
        }
        /**
         * 7.返回最终值
         */
        return allCorpus;
    }

    /**
     * 相似度度计算
     * @param favoriteCorpusList 用户已收藏的语料信息
     * @param corpus 语料库中某类标签的某一个语料
     * @return 返回相似度值
     */
    public double SimilarityCalculator(List<Corpus> favoriteCorpusList, Corpus corpus){
        double maxSimilarity = 0.0;
        for (Corpus favoriteCorpus : favoriteCorpusList) {
            if (favoriteCorpus.getId() != corpus.getId()) {
//                double similarity = calculateTitleSimilarity(favoriteCorpus.getTitle(), corpus.getTitle());//这里是使用本地的相似度算法(已弃用)
                double similarity = cosSimilarity.getSimilarity(favoriteCorpus.getOriginaltext(), corpus.getOriginaltext());//余弦相似度
//                double similarity = editSimilarity.getSimilarity(favoriteCorpus.getOriginaltext(), corpus.getOriginaltext());//编辑距离相似度
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                }
            }
        }
        return maxSimilarity;
    }
    //Levenshtein距离计算两个标题之间的相似度
    public double calculateTitleSimilarity(String title1, String title2) {
        int[][] dp = new int[title1.length() + 1][title2.length() + 1];

        for (int i = 0; i <= title1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= title2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= title1.length(); i++) {
            for (int j = 1; j <= title2.length(); j++) {
                int cost = (title1.charAt(i - 1) == title2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        return 1.0 - (double) dp[title1.length()][title2.length()] / Math.max(title1.length(), title2.length());
    }

}
