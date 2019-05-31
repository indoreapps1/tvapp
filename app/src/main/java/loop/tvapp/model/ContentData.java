package loop.tvapp.model;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import es.dmoral.toasty.Toasty;
import loop.tvapp.MainActivity;
import loop.tvapp.R;
import loop.tvapp.framework.IAsyncWorkCompletedCallback;
import loop.tvapp.framework.ServiceCaller;
import loop.tvapp.utilities.Utility;

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
