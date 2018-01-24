package kr.bydelta.koala.test

import org.json.{JSONArray, JSONObject}

package object pack {

  implicit class JsonObjWrapper(json: JSONObject) {
    def ?(key: String): String = json.getString(key)

    def >>(key: String): JSONObject = json.getJSONObject(key)

    def >>>(key: String): JSONArray = json.getJSONArray(key)
  }

  implicit class JsonArrayWrapper(json: JSONArray) {
    def ?(key: Int): String = json.getString(key)

    def >>(key: Int): JSONObject = json.getJSONObject(key)

    def >>>(key: Int): JSONArray = json.getJSONArray(key)
  }

}
