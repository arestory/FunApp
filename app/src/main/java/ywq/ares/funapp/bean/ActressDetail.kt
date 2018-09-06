package ywq.ares.funapp.bean

class ActressDetail{

    var name :String="不详"
    var avatar :String="不详"
    var birthday:String="不详"
    var age:Int=18
    var cup:String="不详"
    var stature:String="不详"
    var chestWidth:String="不详"
    var waistline:String="不详"
    var hipline :String="不详"
    var home:String="不详"
    var hobby:String="不详"
    override fun toString(): String {
        return "ActressDetail(name=$name, avatar=$avatar, birthday=$birthday, age=$age, stature=$stature, chestWidth=$chestWidth, waistline=$waistline, hipline=$hipline, home=$home, hobby=$hobby)"
    }


}