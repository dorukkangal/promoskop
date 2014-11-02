//
//  UIColor+AppColor.m
//  Promoskop
//
//  Created by Mustafa Besnili on 02/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "UIColor+AppColor.h"

@implementation UIColor (AppColor)
+ (UIColor *) colorFromHexString:(NSString *)hexString {
    NSString *cleanString = [hexString stringByReplacingOccurrencesOfString:@"#" withString:@""];
    if([cleanString length] == 3) {
        cleanString = [NSString stringWithFormat:@"%@%@%@%@%@%@",
                       [cleanString substringWithRange:NSMakeRange(0, 1)],[cleanString substringWithRange:NSMakeRange(0, 1)],
                       [cleanString substringWithRange:NSMakeRange(1, 1)],[cleanString substringWithRange:NSMakeRange(1, 1)],
                       [cleanString substringWithRange:NSMakeRange(2, 1)],[cleanString substringWithRange:NSMakeRange(2, 1)]];
    }
    if([cleanString length] == 6) {
        cleanString = [cleanString stringByAppendingString:@"ff"];
    }
    
    unsigned int baseValue;
    [[NSScanner scannerWithString:cleanString] scanHexInt:&baseValue];
    
    float red = ((baseValue >> 24) & 0xFF)/255.0f;
    float green = ((baseValue >> 16) & 0xFF)/255.0f;
    float blue = ((baseValue >> 8) & 0xFF)/255.0f;
    float alpha = ((baseValue >> 0) & 0xFF)/255.0f;
    
    return [UIColor colorWithRed:red green:green blue:blue alpha:alpha];
}

+(UIColor *)navigationBarColor{
    return [self colorFromHexString:@"#BA1131"];
}

+ (UIColor *)backgroundColor{
    return [self colorFromHexString:@"#DADEC1"];
}

+(UIColor *)basicUIColor{
    return [self colorFromHexString:@"#74B29B"];
}

+(UIColor *)fontColor{
    return [self colorFromHexString:@"#2D2D2D"];
}
@end
