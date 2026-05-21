package com.kwang.climbstyle.domain.banner.repository;

import com.kwang.climbstyle.domain.admin.dto.response.AdminBannerListResponse;
import com.kwang.climbstyle.domain.banner.dto.response.BannerDetailResponse;
import com.kwang.climbstyle.domain.banner.entity.BannerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BannerRepository {

    Integer selectMaxBannerOrder();

    List<BannerDetailResponse> selectVisibleBanners();

    List<AdminBannerListResponse> selectAdminBannerList();

    BannerDetailResponse selectBannerByNo(@Param("bannerNo") Integer bannerNo);

    void insert(BannerEntity bannerEntity);

    void update(BannerEntity bannerEntity);

    void delete(@Param("bannerNo") Integer bannerNo);
}
