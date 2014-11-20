//
//  LeftMenuViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 02/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "LeftMenuViewController.h"
#import "BasicCell.h"
#import "UIImage+Utility.h"
#import <Colours.h>
static NSString * const BasicCellIdentifier = @"BasicCell";

@interface LeftMenuViewController ()
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (strong, nonatomic) NSArray *cellDescriptionArray;

@end



@implementation LeftMenuViewController

- (void)viewDidLoad{
    [[UINavigationBar appearance]setBackgroundColor:[UIColor brickRedColor]];
    [self setTitle:@"Menu"];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    BasicCell *cell = [tableView dequeueReusableCellWithIdentifier:BasicCellIdentifier forIndexPath:indexPath];
    NSDictionary *cellDescription = self.cellDescriptionArray[indexPath.row];
    [cell.leftImageView setImage:[UIImage imageNamed:cellDescription[@"image_name"]] ];
    [cell.descriptionLabel setText:cellDescription[@"description"]];
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.cellDescriptionArray.count;
}

- (NSArray *)cellDescriptionArray{
    if(!_cellDescriptionArray)
        _cellDescriptionArray = [NSArray arrayWithObjects:@{@"image_name" : @"home", @"description" : @"Arama"},@{@"image_name" : @"shopping_cart", @"description" : @"Alışveriş Listesi" }, @{@"image_name" : @"feedback", @"description" : @"Öneri Kutusu"}, nil];
    return _cellDescriptionArray;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.row == 0){
        [self performSegueWithIdentifier:@"HomeViewController" sender:nil];
    }
    else if(indexPath.row == 1){
        [self performSegueWithIdentifier:@"ShoppingListViewController" sender:nil];
    }
    else if(indexPath.row == 2){
        [self performSegueWithIdentifier:@"FeedbackViewController" sender:nil];
    }
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{

}
@end
