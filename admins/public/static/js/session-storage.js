SESSION_KEY_COURSE = "SESSION_KEY_COURSE";
SESSION_KEY_CHAPTER = "SESSION_KEY_CHAPTER";
SESSION_KEY_USER_LOGIN = "SESSION_KEY_USER_LOGIN";

/**
 * 会话缓存
 * @type {{set: SessionStorage.set, get: (function(*=): (any|undefined)), remove: SessionStorage.remove, clearAll: SessionStorage.clearAll}}
 */
SessionStorage = {
    get : function (key){
        let v = sessionStorage.getItem(key);
        if (v && typeof(v) !== "undefined" && v !== "undefined"){
            return JSON.parse(v);
        }
    },
    set : function (key, data) {
        sessionStorage.setItem(key, JSON.stringify(data));
    },
    remove : function (key) {
        sessionStorage.removeItem(key);
    },
    clearAll : function (){
        sessionStorage.clear();
    }
};