package com.ashish.qrscanner.api

import com.google.gson.annotations.SerializedName

data class JokeModel(@SerializedName("error"  ) var error  : Boolean?         = null,
                     @SerializedName("amount" ) var amount : Int?             = null,
                     @SerializedName("jokes"  ) var jokes  : ArrayList<Jokes> = arrayListOf())


data class Flags (

    @SerializedName("nsfw"      ) var nsfw      : Boolean? = null,
    @SerializedName("religious" ) var religious : Boolean? = null,
    @SerializedName("political" ) var political : Boolean? = null,
    @SerializedName("racist"    ) var racist    : Boolean? = null,
    @SerializedName("sexist"    ) var sexist    : Boolean? = null,
    @SerializedName("explicit"  ) var explicit  : Boolean? = null

)

data class Jokes (

    @SerializedName("category" ) var category : String?  = null,
    @SerializedName("type"     ) var type     : String?  = null,
    @SerializedName("setup"    ) var setup    : String?  = null,
    @SerializedName("delivery" ) var delivery : String?  = null,
    @SerializedName("flags"    ) var flags    : Flags?   = Flags(),
    @SerializedName("id"       ) var id       : Int?     = null,
    @SerializedName("safe"     ) var safe     : Boolean? = null,
    @SerializedName("lang"     ) var lang     : String?  = null

)