package com.kwang.climbstyle.common.protocal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonListRequest extends PaginationRequest {

    private String pageNo;

    private String searchType;

    private String searchName;

    @Override
    public String getPageNo() {
        return this.pageNo;
    }

    @Override
    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public void setTotalCount(int totalCount) {
        super.setTotalCount(totalCount);
    }

    public int getPageNoAsInt() {
        if (pageNo == null || pageNo.trim().isEmpty()) {
            return 1;
        }
        try {
            int num = Integer.parseInt(pageNo.trim());
            if (num > 0) {
                return num;
            }
            return 1;
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
