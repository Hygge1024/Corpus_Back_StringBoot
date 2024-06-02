package com.lt.service.impl;

import com.lt.domain.Corpus;
import com.lt.domain.Tag;
import com.lt.service.CorpusService;
import com.lt.service.KnowledgeGraphConverter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class KnowledgeGraphConverterImpl implements KnowledgeGraphConverter {

    @Autowired
    private CorpusService corpusService;

    @Override
    public JSONArray toKnowledgeGraphConverter() {
        // 从数据库中获取语料列表
        log.info("正在搜索语料corpuslist");

        JSONArray resultArray = new JSONArray();
        List<Corpus> corpusList = corpusService.getAllCorpus();

        // 如果语料列表为空，返回空的JSONArray，并打印错误信息
        if (corpusList == null) {
            log.error("语料列表为空");
            JSONObject errorObject = new JSONObject();
            errorObject.put("error", "Corpus list is null.");
            resultArray.put(errorObject);
            return resultArray;
        }
        log.info("正在转换语料为知识图谱格式");
        // 遍历语料列表，将每个语料转换成知识图谱的JSON格式
        for (Corpus corpus : corpusList) {
            // 如果语料为空，跳过当前语料的转换
            if (corpus == null) {
                log.warn("语料为空，跳过当前语料的转换");
                continue;
            }
            //标签tagid与语料关联
            List<Tag> tagList = corpus.getTag_ids();

            // 创建start、end、relationship等JSONObject对象，表示知识图谱中的节点和关系
            JSONObject pObject = new JSONObject();//创建一个 JSON 对象，表示知识图谱中的一个节点或关系。

            JSONObject startObject = new JSONObject();//创建一个 JSON 对象，表示起始节点。
            startObject.put("identity", corpus.getId());//将起始节点的标识属性设置为语料的 ID。
            startObject.put("labels", new JSONArray(Arrays.asList("Enterprise")));//将起始节点的标签属性设置为 "Enterprise"。

            JSONObject startProperties = new JSONObject();//创建一个 JSON 对象，表示起始节点的属性。
            startProperties.put("name", corpus.getTitle());//将起始节点的名称属性设置为语料的标题。
            startProperties.put("setup_time", corpus.getAuthorID()); //将起始节点的设置时间属性设置为语料的作者ID（假设作者ID即为设置时间,前端目前是这样的格式,参照前端的样板写的）。
            startProperties.put("address", corpus.getPublished_at()); //将起始节点的地址属性设置为语料的发布时间。
            switch (corpus.getDifficulty()){//将起始节点的 captial 属性设置为固定值 "简单"。
                case 1:
                    startProperties.put("captial", "简单");
                    break;
                case 2:
                    startProperties.put("captial", "中等");
                    break;
                case 3:
                    startProperties.put("captial", "困难");
                    break;
                default:
                    startProperties.put("captial", "未知");
                    break;
            }
            //放什么待考虑
            startProperties.put("credit_code", "48秒"); //将起始节点的 credit_code 属性设置为固定值 "48秒"。
            startObject.put("properties", startProperties);//将起始节点的属性设置为前面创建的属性对象。


            JSONObject endObject = new JSONObject();//创建结束节点的 JSON 对象和属性对象。
            endObject.put("identity", corpus.getType()); // Assuming Type as identity
            endObject.put("labels", new JSONArray(Arrays.asList("Type")));

            JSONObject endProperties = new JSONObject();
//            endProperties.put("name", "真题类"); // Assuming a fixed value
            switch (corpus.getType()) {
                case 1:
                    endProperties.put("name", "同传");
                    break;
                case 2:
                    endProperties.put("name", "交传");
                    break;
                case 3:
                    endProperties.put("name", "会议口译");
                    break;
                case 4:
                    endProperties.put("name", "陪同口译");
                    break;
                default:
                    endProperties.put("name", "未知类型");
                    break;
            }
            endObject.put("properties", endProperties);

            //关联start和end
            JSONObject relationshipObject = new JSONObject();//创建一个 JSON 对象，表示节点之间的关系。
            SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(corpus.getId()%32); // 传入一个workerId
            long randomIdentity = idGenerator.nextId();
            relationshipObject.put("identity", randomIdentity); //将关系的标识属性设置为固定值 621275
            relationshipObject.put("start", corpus.getId());//将关系的起始节点设置为语料的 ID
            relationshipObject.put("end", corpus.getType());//将关系的结束节点设置为语料的类型
            relationshipObject.put("type", "type");//将关系的类型设置为 "type"。
            relationshipObject.put("properties", new JSONObject(Collections.singletonMap("name", "类型")));//将关系的属性设置为一个 JSON 对象，表示关系的类型为 "类型"。


            JSONObject segmentObject = new JSONObject();//创建一个 JSON 对象，表示一段知识图谱中的信息。
            segmentObject.put("start", startObject);//将段落的起始节点设置为起始节点的 JSON 对象。
            segmentObject.put("relationship", relationshipObject);//将段落的关系设置为关系的 JSON 对象。
            segmentObject.put("end", endObject);//将段落的结束节点设置为结束节点的 JSON 对象。

            pObject.put("start", startObject);
            pObject.put("end", endObject);
            pObject.put("segments", new JSONArray(Arrays.asList(segmentObject)));
            pObject.put("length", 1.0);

            // 将当前语料转换后的知识图谱对象放入结果JSONArray中
            JSONObject resultObject = new JSONObject();
            resultObject.put("p", pObject);
            resultObject.put("score", 2.307142857142857); // Assuming a fixed value
            resultArray.put(resultObject);

            //单独对corpus——tag_ids进行关联
            for(Tag tag : tagList){
//                log.info("当前corpus"+corpus.getId()+" tag:"+tag.getTagName());
                JSONObject tagNode = new JSONObject();
                tagNode.put("identity",tag.getId());
                tagNode.put("labels",new JSONArray(Arrays.asList("Tag")));

                JSONObject tagProperties = new JSONObject(); // 创建一个 JSON 对象，表示标签节点的属性
                tagProperties.put("name",tag.getTagName());
                tagProperties.put("about",tag.getIntroduce());

                tagNode.put("properties",tagProperties);

                // 创建与标签节点相关联的关系
                JSONObject tagRelationship = new JSONObject(); // 创建一个 JSON 对象，表示节点之间的关系
                SnowflakeIdGenerator idGenerator2 = new SnowflakeIdGenerator(corpus.getId()%16); // 传入一个workerId
                randomIdentity = idGenerator2.nextId();
                tagRelationship.put("identity", randomIdentity); // 将关系的标识属性设置为随机生成的值
                tagRelationship.put("start", corpus.getId()); // 将关系的起始节点设置为语料节点的 ID
                tagRelationship.put("end", tag.getId()); // 将关系的结束节点设置为标签节点的 ID
                tagRelationship.put("type", "has_tag"); // 将关系的类型设置为 "has_tag"
                tagRelationship.put("properties", new JSONObject(Collections.singletonMap("name", "标签")));//将关系的属性设置为一个 JSON 对象，表示关系的类型为 "标签"。


                // 创建段落对象，并将标签节点和关系添加到段落中
                JSONObject tagSegment = new JSONObject();

                tagSegment.put("start", startObject);
                tagSegment.put("relationship", tagRelationship);
                tagSegment.put("end", tagNode);
//
//                pObject.put("start", startObject);
//                pObject.put("end", tagNode);
//                pObject.getJSONArray("segments").put(tagSegment);// 将段落对象添加到 pObject 中的 segments 数组中
//                pObject.put("length", 2.0);

                JSONObject pObjectTag = new JSONObject();//创建一个 JSON 对象，表示知识图谱中的一个节点或关系。
                pObjectTag.put("start", startObject);
                pObjectTag.put("end", tagNode);
                pObjectTag.put("segments", new JSONArray(Arrays.asList(tagSegment)));
                pObjectTag.put("length", 2.0);


//                resultObject.put("p", pObject);
//                resultObject.put("score", 2.307142857142857); // Assuming a fixed value
//                resultArray.put(resultObject);
                JSONObject resultObjectTag = new JSONObject();
                resultObjectTag.put("p", pObjectTag);
                resultObjectTag.put("score", 2.307142857142857); // Assuming a fixed value
                resultArray.put(resultObjectTag);
            }
        }

        // 打印转换后的知识图谱结果，并返回结果JSONArray
        log.info("搜索结果为:" + resultArray.toString(4));
        return resultArray;
    }
    //雪花算法
    public class SnowflakeIdGenerator {
        private final long workerId;
        private final long epoch = 1622664000000L; // 2021-06-03 00:00:00 的时间戳
        private long sequence = 0L;
        private final long workerIdBits = 5L;
        private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
        private final long timestampBits = 41L;
        private final long sequenceBits = 12L;
        private final long workerIdShift = sequenceBits;
        private final long timestampLeftShift = sequenceBits + workerIdBits;
        private final long sequenceMask = -1L ^ (-1L << sequenceBits);
        private long lastTimestamp = -1L;

        public SnowflakeIdGenerator(long workerId) {
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format("Worker ID can't be greater than %d or less than 0", maxWorkerId));
            }
            this.workerId = workerId;
        }

        public synchronized long nextId() {
            long timestamp = System.currentTimeMillis();

            if (timestamp < lastTimestamp) {
                throw new RuntimeException("Clock moved backwards. Refusing to generate id");
            }

            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                if (sequence == 0) {
                    // 如果在当前时间戳下的序列号已经达到最大值，等待到下一毫秒
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                // 如果是新的时间戳，则从序列号的最低位开始
                sequence = 0L;
            }

            lastTimestamp = timestamp;

            // 生成最终的ID，并返回
            return ((timestamp - epoch) << timestampLeftShift) |
                    (workerId << workerIdShift) |
                    sequence;
        }

        private long tilNextMillis(long lastTimestamp) {
            long timestamp = System.currentTimeMillis();
            while (timestamp <= lastTimestamp) {
                timestamp = System.currentTimeMillis();
            }
            return timestamp;
        }
    }


}
