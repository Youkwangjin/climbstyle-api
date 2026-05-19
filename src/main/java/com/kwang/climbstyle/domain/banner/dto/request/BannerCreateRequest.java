package com.kwang.climbstyle.domain.banner.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BannerCreateRequest {

    private List<MultipartFile> bannerImages;
}
