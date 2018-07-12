package com.sfzd5.amtbtv.util;

import com.sfzd5.amtbtv.model.Result;

public interface AmtbApiCallBack<T extends Result> {
    void callBack(T obj);
}
