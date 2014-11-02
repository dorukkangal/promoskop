//
//  UIColor+AppColor.h
//  Promoskop
//
//  Created by Mustafa Besnili on 02/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIColor (AppColor)
+ (UIColor *) colorFromHexString:(NSString *)hexString;
+ (UIColor *)navigationBarColor;
+ (UIColor *)backgroundColor;
+ (UIColor *)basicUIColor;
+ (UIColor *)fontColor;
@end
