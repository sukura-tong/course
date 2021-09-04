package com.swust.server.enums;

public enum ChapterEnum {

    CHECK(0,"核验数据"),
    SUCCESS(1,"操作成功"),
    ERROR(2,"操作异常");

    private int state;
    private String stateInfo;

    private ChapterEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ChapterEnum getOffs(int state){
        for (ChapterEnum chapterEnum : ChapterEnum.values()){
            if (chapterEnum.getState() == state){
                return chapterEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
