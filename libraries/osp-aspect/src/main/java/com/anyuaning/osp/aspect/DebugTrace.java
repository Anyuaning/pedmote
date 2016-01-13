package com.anyuaning.osp.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by thom on 1/13/16.
 */

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface DebugTrace {}