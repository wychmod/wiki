package com.wychmod.wiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface DocMapperCust {
    public void increaseVoteCount(@Param("id") Long id);

    public void increaseViewCount(@Param("id") Long id);
}

