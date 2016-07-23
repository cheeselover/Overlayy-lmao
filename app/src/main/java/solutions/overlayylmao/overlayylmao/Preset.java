package solutions.overlayylmao.overlayylmao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daniel on 2016-07-23.
 */

public class Preset implements Parcelable {
    String title;
    int updateTime;
    int verticalGravity;
    int horizontalGravity;
    int xOffset;
    int yOffset;
    int width;
    int height;
    boolean coverStatusBar;
    boolean coverNavBar;
    int rotation;
    int scaleX;
    int scaleY;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(this.updateTime);
        dest.writeInt(this.verticalGravity);
        dest.writeInt(this.horizontalGravity);
        dest.writeInt(this.xOffset);
        dest.writeInt(this.yOffset);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeByte(this.coverStatusBar ? (byte) 1 : (byte) 0);
        dest.writeByte(this.coverNavBar ? (byte) 1 : (byte) 0);
        dest.writeInt(this.rotation);
        dest.writeInt(this.scaleX);
        dest.writeInt(this.scaleY);
    }

    public Preset() {
    }

    protected Preset(Parcel in) {
        this.title = in.readString();
        this.updateTime = in.readInt();
        this.verticalGravity = in.readInt();
        this.horizontalGravity = in.readInt();
        this.xOffset = in.readInt();
        this.yOffset = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
        this.coverStatusBar = in.readByte() != 0;
        this.coverNavBar = in.readByte() != 0;
        this.rotation = in.readInt();
        this.scaleX = in.readInt();
        this.scaleY = in.readInt();
    }

    public static final Parcelable.Creator<Preset> CREATOR = new Parcelable.Creator<Preset>() {
        @Override
        public Preset createFromParcel(Parcel source) {
            return new Preset(source);
        }

        @Override
        public Preset[] newArray(int size) {
            return new Preset[size];
        }
    };
}
