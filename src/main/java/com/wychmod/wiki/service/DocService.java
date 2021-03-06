package com.wychmod.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wychmod.wiki.domain.Content;
import com.wychmod.wiki.domain.Doc;
import com.wychmod.wiki.domain.DocExample;
import com.wychmod.wiki.exception.BusinessException;
import com.wychmod.wiki.exception.BusinessExceptionCode;
import com.wychmod.wiki.mapper.ContentMapper;
import com.wychmod.wiki.mapper.DocMapper;
import com.wychmod.wiki.mapper.DocMapperCust;
import com.wychmod.wiki.req.DocQueryReq;
import com.wychmod.wiki.req.DocSaveReq;
import com.wychmod.wiki.resp.DocQueryResp;
import com.wychmod.wiki.resp.PageResp;
import com.wychmod.wiki.util.CopyUtil;
import com.wychmod.wiki.util.RedisUtil;
import com.wychmod.wiki.util.RequestContext;
import com.wychmod.wiki.util.SnowFlake;
import com.wychmod.wiki.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private DocMapper docMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private DocMapperCust docMapperCust;

    @Resource
    private WebSocketServer webSocketServe;

    @Resource
    private WsService wsService;

//    @Resource
//    private RocketMQTemplate rocketMQTemplate;

    public List<DocQueryResp> all(long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return list;
    }

    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        // ?????? pagenum???????????? pagesize????????? ??????pagehelper????????????????????????????????????
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);
        // PageInfo ?????????????????????????????????
        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("????????????{}",pageInfo.getTotal());
        LOG.info("????????????{}",pageInfo.getPages());

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * ??????
     * @param req
     */
    @Transactional
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())){
            // ??????
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            // ??????
            docMapper.updateByPrimaryKey(doc);
            // ????????????????????????
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count==0){
                contentMapper.insert(content);
            }
        }
    }

    public void delete(long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> idS) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(idS);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        // ???????????????+1
        docMapperCust.increaseViewCount(id);
        if (!ObjectUtils.isEmpty(content)) {
            return content.getContent();
        }
        return "";
    }

    public void vote(long id) {
        // ??????IP+doc.id??????key???24?????????????????????
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 24)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        //????????????
        Doc docDb = docMapper.selectByPrimaryKey(id);
        String logId = MDC.get("LOG_ID");
//        rocketMQTemplate.convertAndSend("VOTE_TOPIC","???"+docDb.getName()+"????????????");
        wsService.sendInfo("???"+docDb.getName()+"????????????!", logId);
    }

    public void updateEbookInfo(){
        docMapperCust.updateEbookInfo();
    }
}
