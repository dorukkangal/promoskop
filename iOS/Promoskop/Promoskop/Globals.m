//
//  Globals.m
//  Promoskop
//
//  Created by Mustafa Besnili on 22/10/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "Globals.h"

@implementation Globals
//NSString *baseURL = @"http://104.131.180.136:8080/promoskop/product/";
//NSString *baseURL = @"https://fathomless-atoll-1063.herokuapp.com/api/products/";
NSString *baseURL = @"http://promoskop.elasticbeanstalk.com/";
//NSString *baseURL = @"http://192.168.1.4:8080/promoskop-web-service/";
NSString *findBySubString = @"product/findBySubString?text=";
NSString *findByID = @"product/";
NSString *getPopularProducts = @"product/popular?count=";
NSString *calculate = @"product/basket/calculate";
NSString *config = @"config";
NSString *feedback = @"feedback";
@end
