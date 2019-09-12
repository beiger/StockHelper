package com.fanhantech.baselib.utils;

import com.fanhantech.baselib.app.BaseApplication;

import java.io.File;

public class BaseConstant {
	public static final String File_DIR = BaseApplication.context.getFilesDir() + File.separator + "data" + File.separator;
	public static final String Out_File_DIR = BaseApplication.context.getExternalCacheDir() + File.separator + "data" + File.separator;
}
