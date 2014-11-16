//
//  FeedbackViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 05/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "FeedbackViewController.h"
#import <Colours.h>
#import "Globals.h"
#import <AFNetworking.h>
#import "UITextFieldWithPadding.h"

@interface FeedbackViewController()<UIAlertViewDelegate>
@property (weak, nonatomic) IBOutlet UITextFieldWithPadding *emailTextField;
@property (weak, nonatomic) IBOutlet UITextView *feedbackTextView;
@end

@implementation FeedbackViewController

- (void)viewDidLoad{
    [self.navigationItem setTitle:@"Öneri Kutusu"];
}

- (IBAction)backButtonPressed:(id)sender {
    [self dismissViewControllerAnimated:YES completion:NULL];
}


- (IBAction)sendFeedback:(id)sender {
    if([self.feedbackTextView.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]].length == 0){
        UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:@"Hata" message:@"Geri bildirim alanı boş bırakılamaz" delegate:nil cancelButtonTitle:@"Tamam" otherButtonTitles:nil, nil];
        [alertView show];
    }
    else{
        NSDictionary *postParameters = @{@"email" : self.emailTextField.text ,
                                         @"feedback" : self.feedbackTextView.text};
        AFSecurityPolicy *policy = [[AFSecurityPolicy alloc] init];
        [policy setAllowInvalidCertificates:YES];
        AFHTTPRequestOperationManager *operationManager = [AFHTTPRequestOperationManager manager];
        operationManager.requestSerializer = [AFJSONRequestSerializer serializer];
        [operationManager setSecurityPolicy:policy];
        [operationManager POST:[baseURL stringByAppendingString:feedback] parameters:postParameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
            if([operation.response statusCode] == 200){
                UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:@"Info" message:@"Geri bildiriminiz alınmıştur" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil] ;
                [alertView show];
            }
            [self.navigationController dismissViewControllerAnimated:YES completion:nil];
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            NSLog(@"[FeedbackViewController] Error : %@", [error description]);
            UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:@"Hata" message:@"Bir hata oluştu. Lütfen daha sonra tekrar deneyiniz" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil] ;
            [alertView show];
        }];
    }
}

@end
