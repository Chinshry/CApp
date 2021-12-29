package com.chinshry.base.bean

/**
 * Created by chinshry on 2021/12/23.
 * Describe： 证件枚举类
 */
enum class CardType(var cardName: String, var sxType: String, var tyrzType: String) {
    ID_CARD("身份证","0","01"),
    MILITARY_CERTIFICATE ("军官证","1","03"),
    PASSPORT("护照","2","02"),
    DRIVING_LICENSE("驾照","3","08"),
    OTHER("其他","4",""),
    FAKE_ID_CARD("伪身份证","5",""),
    HONG_KONG_MACAU_PASS ("港澳居民通行证","6","04"),
    TAIWAN_PASS ("台湾居民通行证","7","05"),
    RESIDENCE_BOOKLET("户口簿","10","09"),
    BIRTH_CERTIFICATE("出生医学证明","11","10"),
    SOLDIER_CARD("士兵证","12","11"),
    FOREIGN_PERMANENT_RESIDENT("外国人永久居留证","13","06"),
    FOREIGN_ID_CARD("外国人身份证","91",""),
    HONG_KONG_MACAU_ID_CARD("港澳居民居住证","15","646"),
    TAIWAN_ID_CARD ("台湾居民居住证","16","647");

    enum class CHANNEL {
        SX, TYRZ
    }

    companion object {

        fun getNames():MutableList<String> {
            val nameList = mutableListOf<String>()
            values().forEach {
                nameList.add(it.cardName)
            }
            return nameList
        }

        fun getCardNameByType(type: String?, channelType: CHANNEL): String {
            for (each in values()) {
                if (getTypeByChannel(each, channelType) == type) {
                    return each.cardName
                }
            }
            return ""
        }

        fun getCardTypeByName(name: String?, channelType: CHANNEL): String {
            for (each in values()) {
                if (each.cardName == name) {
                    return getTypeByChannel(each, channelType)
                }
            }
            return ""
        }

        fun convertCardType(type: String?, fromChannelType: CHANNEL, toChannelType: CHANNEL): String {
            for (each in values()) {
                if (getTypeByChannel(each, fromChannelType) == type) {
                    return getTypeByChannel(each, toChannelType)
                }
            }
            return getTypeByChannel(OTHER, toChannelType)
        }

        fun getTypeByChannel(each: CardType, channelType: CHANNEL): String {
            return when (channelType) {
                CHANNEL.SX -> each.sxType
                CHANNEL.TYRZ -> each.tyrzType
            }

        }

        fun getCardNameListByTypeList(typeList : List<String>?, channelType: CHANNEL): List<String> {
            if (typeList.isNullOrEmpty()) {
                return mutableListOf()
            }
            val list = mutableListOf<String>()
            typeList.forEach {
                val cardName = getCardNameByType(it, channelType)
                if (cardName.isNotBlank()) {
                    list.add(cardName)
                }
            }
            return  list
        }
    }
}