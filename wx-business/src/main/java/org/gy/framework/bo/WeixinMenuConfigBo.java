package org.gy.framework.bo;

public class WeixinMenuConfigBo {

    /**
     * 主键
     * 
     */
    private Long   id;

    /**
     * 菜单名称
     * 
     */
    private String menuName;

    /**
     * 菜单类型
     * 
     */
    private String menuType;

    /**
     * 父层ID
     * 
     */
    private Long   parentId;

    /**
     * 回复ID
     * 
     */
    private Long   replyId;

    /**
     * 排序码
     * 
     */
    private Byte   sortNo;

    /**
     * 标题
     * 
     */
    private String title;

    /**
     * 描述
     * 
     */
    private String description;

    /**
     * 回复文本
     * 
     */
    private String replyText;

    /**
     * 回复链接
     * 
     */
    private String replyLink;

    /**
     * 回复图片
     * 
     */
    private String replyImg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Byte getSortNo() {
        return sortNo;
    }

    public void setSortNo(Byte sortNo) {
        this.sortNo = sortNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public String getReplyLink() {
        return replyLink;
    }

    public void setReplyLink(String replyLink) {
        this.replyLink = replyLink;
    }

    public String getReplyImg() {
        return replyImg;
    }

    public void setReplyImg(String replyImg) {
        this.replyImg = replyImg;
    }
    
    

}
