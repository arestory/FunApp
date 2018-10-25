package ywq.ares.funapp.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

open class CollectionBean<T>:MultiItemEntity{


     var isCollected : Boolean = false

     var dataBean:T?=null

     override fun getItemType(): Int {
          return when(dataBean is MovieSearchItem){
               true -> 1
               false ->2

          }
     }
}
