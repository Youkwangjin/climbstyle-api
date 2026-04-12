package com.kwang.climbstyle.domain.notice.dto.request;

import com.kwang.climbstyle.common.protocal.CommonListRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeDetailRequest extends CommonListRequest {

    private String noticeCategory;
}
