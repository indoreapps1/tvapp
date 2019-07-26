package loop.tvapp.model;

public class ContentData  {
    private String img_vid;

    private String video;

    private String cid;

    public String getImg_vid ()
    {
        return img_vid;
    }

    public void setImg_vid (String img_vid)
    {
        this.img_vid = img_vid;
    }

    public String getVideo ()
    {
        return video;
    }

    public void setVideo (String video)
    {
        this.video = video;
    }

    public String getCid ()
    {
        return cid;
    }

    public void setCid (String cid)
    {
        this.cid = cid;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [img_vid = "+img_vid+", video = "+video+", cid = "+cid+"]";
    }
}
