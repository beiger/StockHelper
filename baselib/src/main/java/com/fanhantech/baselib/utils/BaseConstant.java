package com.fanhantech.baselib.utils;

import android.os.Environment;
import com.fanhantech.baselib.app.BaseApplication;

import java.io.File;

public class BaseConstant {
	public static final String File_DIR = BaseApplication.context.getFilesDir() + File.separator + "data" + File.separator;
	public static final String OUT_DOCUMENT_DIR = BaseApplication.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + File.separator;
}
