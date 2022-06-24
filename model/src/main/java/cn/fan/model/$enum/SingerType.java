package cn.fan.model.$enum;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
public enum SingerType implements ICodeEnum{

    MALE(0,"男"),
    FEMALE(1,"女"),
    TEAM(2,"组合"),
    UN_KNOW(-1,"未知类型");
    private int code;
    private String desc;

    private SingerType(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static SingerType valueOf(int code){
        SingerType singerType;
        switch (code){
            case 0:
                singerType=MALE;
                break;
            case 1:
                singerType=FEMALE;
                break;
            case 2:
                singerType=TEAM;
                break;
            default:
                singerType=UN_KNOW;
                break;
        }
        return singerType;
    }
}
