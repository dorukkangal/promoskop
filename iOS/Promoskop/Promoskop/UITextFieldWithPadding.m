//
//  UITextFieldWithPadding.m
//  Promoskop
//
//  Created by Mustafa Besnili on 07/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "UITextFieldWithPadding.h"

@implementation UITextFieldWithPadding
- (CGRect)textRectForBounds:(CGRect)bounds
{
    return CGRectInset(bounds, 10.0f, 0);
}

- (CGRect)editingRectForBounds:(CGRect)bounds
{
    return [self textRectForBounds:bounds];
}

@end
