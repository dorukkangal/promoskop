//
//  SearchedProductsViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 15/10/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "SearchedProductsViewController.h"
#import "ProductWithPriceDetailTableViewController.h"

@interface SearchedProductsViewController ()<UITableViewDataSource,UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *productsTableView;
@end

@implementation SearchedProductsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    NSLog(@"FoundProducts => %@",self.foundProducts);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.foundProducts.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"SearchedProducts" forIndexPath:indexPath];
    NSDictionary *dict = self.foundProducts[indexPath.row];
    [cell.textLabel setText:dict[@"name"]];
    return cell;
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if([segue.identifier isEqualToString:@"ProductWithPriceDetailTableViewController"]){
        UITableViewCell *cell = (UITableViewCell *)sender;
        NSIndexPath *indexPath = [self.productsTableView indexPathForCell:cell];
        NSDictionary *dict = self.foundProducts[indexPath.row];
        ProductWithPriceDetailTableViewController *productWithPriceDetailTableViewController = (ProductWithPriceDetailTableViewController *)segue.destinationViewController;
        [productWithPriceDetailTableViewController setProductID:[dict[@"id"] integerValue]];
    }
    
}


@end
