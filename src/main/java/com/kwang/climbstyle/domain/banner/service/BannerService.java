package com.kwang.climbstyle.domain.banner.service;

import com.kwang.climbstyle.code.banner.BannerErrorCode;
import com.kwang.climbstyle.code.banner.BannerVisibleStatus;
import com.kwang.climbstyle.code.file.FileTypeCode;
import com.kwang.climbstyle.common.util.SecurityUtil;
import com.kwang.climbstyle.domain.admin.dto.response.AdminBannerListResponse;
import com.kwang.climbstyle.domain.banner.dto.request.BannerCreateRequest;
import com.kwang.climbstyle.domain.banner.dto.request.BannerUpdateRequest;
import com.kwang.climbstyle.domain.banner.dto.response.BannerDetailResponse;
import com.kwang.climbstyle.domain.banner.entity.BannerEntity;
import com.kwang.climbstyle.domain.banner.repository.BannerRepository;
import com.kwang.climbstyle.domain.file.service.FileService;
import com.kwang.climbstyle.exception.ClimbStyleException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    private final FileService fileService;

    @Transactional(readOnly = true)
    public List<BannerDetailResponse> getVisibleBanners() {
        return bannerRepository.selectVisibleBanners();
    }

    @Transactional(readOnly = true)
    public List<AdminBannerListResponse> getAdminBannerList() {
        return bannerRepository.selectAdminBannerList();
    }

    @Transactional(readOnly = true)
    public BannerDetailResponse getAdminBannerDetail(Integer bannerNo) {
        BannerDetailResponse banner = bannerRepository.selectBannerByNo(bannerNo);
        if (banner == null) {
            throw new ClimbStyleException(BannerErrorCode.BANNER_NOT_FOUND);
        }

        return banner;
    }

    @Transactional
    public void createBanner(BannerCreateRequest request) {
        final Integer adminNo = SecurityUtil.getCurrentAdminNo();
        final List<MultipartFile> bannerImages = request.getBannerImages();
        final LocalDateTime bannerCreated = LocalDateTime.now();
        final String bannerVisibleYn = BannerVisibleStatus.VISIBLE.getCode();

        if (bannerImages != null && !bannerImages.isEmpty()) {
            int nextOrder = bannerRepository.selectMaxBannerOrder() + 1;

            for (int i = 0; i < bannerImages.size(); i++) {
                final MultipartFile bannerImage = bannerImages.get(i);

                final String bannerFileOriginalName = FilenameUtils.getName(bannerImage.getOriginalFilename());
                final String bannerFileExtnsNm = FilenameUtils.getExtension(bannerFileOriginalName);
                final String bannerFileStoredName = String.format("banner_%s.%s",
                        UUID.randomUUID().toString().replaceAll("-", ""),
                        bannerFileExtnsNm);
                final Integer bannerFileSortOrder = nextOrder + i;

                String bannerImagePath = fileService.fileUpload(bannerImage, FileTypeCode.BANNER, bannerFileStoredName);

                BannerEntity bannerEntity = BannerEntity.builder()
                        .adminNo(adminNo)
                        .bannerImagePath(bannerImagePath)
                        .bannerOrder(bannerFileSortOrder)
                        .bannerVisibleYn(bannerVisibleYn)
                        .bannerCreated(bannerCreated)
                        .build();

                bannerRepository.insert(bannerEntity);
            }
        }
    }

    @Transactional
    public void updateBanner(Integer bannerNo, BannerUpdateRequest request) {
        final Integer newBannerOrder = request.getBannerOrder();
        final String newBannerVisibleYn = request.getBannerVisibleYn();
        final LocalDateTime bannerUpdated = LocalDateTime.now();

        BannerDetailResponse banner = bannerRepository.selectBannerByNo(bannerNo);
        if (banner == null) {
            throw new ClimbStyleException(BannerErrorCode.BANNER_NOT_FOUND);
        }

        BannerEntity bannerEntity = BannerEntity.builder()
                .bannerNo(bannerNo)
                .bannerOrder(newBannerOrder)
                .bannerVisibleYn(newBannerVisibleYn)
                .bannerUpdated(bannerUpdated)
                .build();

        bannerRepository.update(bannerEntity);
    }

    @Transactional
    public void deleteBanner(Integer bannerNo) {
        BannerDetailResponse banner = bannerRepository.selectBannerByNo(bannerNo);
        if (banner == null) {
            throw new ClimbStyleException(BannerErrorCode.BANNER_NOT_FOUND);
        }

        fileService.fileDelete(banner.getBannerImagePath());
        bannerRepository.delete(bannerNo);
    }
}
