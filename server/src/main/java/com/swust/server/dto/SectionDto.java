package com.swust.server.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class SectionDto {
    /**
    * ID
    */
    private String id;
    /**
    * 标题
    */
    private String title;
    /**
    * 课程|course.id
    */
    private String courseId;
    /**
    * 大章|chapter.id
    */
    private String chapterId;
    /**
    * 视频
    */
    private String video;
    /**
    * 时长|单位秒
    */
    private Integer time;
    /**
    * 收费|C收费F免费
    */
    private String charge;
    /**
    * 顺寻
    */
    private Integer sort;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createAt;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateAt;

    private String vod;

    public String getVod() {
        return vod;
    }

    public void setVod(String vod) {
        this.vod = vod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SectionDto{");
        sb.append("id='").append(id).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", courseId='").append(courseId).append('\'');
        sb.append(", chapterId='").append(chapterId).append('\'');
        sb.append(", video='").append(video).append('\'');
        sb.append(", time=").append(time);
        sb.append(", charge='").append(charge).append('\'');
        sb.append(", sort=").append(sort);
        sb.append(", createAt=").append(createAt);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", vod='").append(vod).append('\'');
        sb.append('}');
        return sb.toString();
    }
}