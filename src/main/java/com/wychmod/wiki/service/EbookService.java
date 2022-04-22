package com.wychmod.wiki.service;

import com.wychmod.wiki.domain.Ebook;
import com.wychmod.wiki.domain.EbookExample;
import com.wychmod.wiki.mapper.EbookMapper;
import com.wychmod.wiki.req.EbookReq;
import com.wychmod.wiki.resp.EbookResp;
import com.wychmod.wiki.util.CopyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+req.getName()+"%");
        List<Ebook> ebooklist = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> list = CopyUtil.copyList(ebooklist, EbookResp.class);
        return list;
    }
}
