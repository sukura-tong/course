LOCAL_KEY_LOGIN_USER_REMEBER = "LOCAL_KEY_LOGIN_USER_REMEBER"

/**
 * 本地缓存
 * @type {{set: LocalStorage.set, get: (function(*=): (any|undefined)), remove: LocalStorage.remove, clearAll: LocalStorage.clearAll}}
 */
LocalStorage = {
    get : function (key){
        let v = localStorage.getItem(key);
        if (v && typeof(v) !== "undefined" && v !== "undefined"){
            return JSON.parse(v);
        }
    },
    set : function (key, data) {
        localStorage.setItem(key, JSON.stringify(data));
    },
    remove : function (key) {
        localStorage.removeItem(key);
    },
    clearAll : function (){
        localStorage.clear();
    }
};