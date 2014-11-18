//
//  BarcodeScannerViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 15/10/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "BarcodeScannerViewController.h"
#import "ProductWithPriceDetailViewController.h"
#import "DataAccessLayer.h"
#import <AVFoundation/AVFoundation.h>

@interface BarcodeScannerViewController ()<AVCaptureMetadataOutputObjectsDelegate>
@property (weak, nonatomic) IBOutlet UILabel *scanDescriptionLabel;
@property (nonatomic,strong) NSString *scannedBarcode;
@property (weak, nonatomic) IBOutlet UIImageView *imageView;
@property (weak, nonatomic) IBOutlet UIImageView *upperImageView;
@property (weak, nonatomic) IBOutlet UIImageView *scannerImageView;


@property (nonatomic, strong) AVCaptureDevice *captureDevice;
@property (nonatomic, strong) AVCaptureDeviceInput *captureDeviceInput;
@property (nonatomic, strong) AVCaptureMetadataOutput *captureMetadataOutput;
@property (nonatomic, strong) AVCaptureSession *captureSession;
@property (nonatomic, strong) AVCaptureVideoPreviewLayer *previewLayer;
@end

@implementation BarcodeScannerViewController

- (void)viewDidLoad {
    [super viewDidLoad];    
    [self setupScanner];
    if(!self.captureSession.isRunning)
        [self.captureSession startRunning];
    [self.view bringSubviewToFront:self.scanDescriptionLabel];
}

- (void)setupScanner{
    
    self.captureDevice = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    self.captureDeviceInput = [AVCaptureDeviceInput deviceInputWithDevice:self.captureDevice error:nil];
    
    self.captureMetadataOutput = [[AVCaptureMetadataOutput alloc] init];
    
    self.captureSession = [[AVCaptureSession alloc] init];
    [self.captureSession addOutput:self.captureMetadataOutput];
    [self.captureSession addInput:self.captureDeviceInput];
    
    [self.captureMetadataOutput setMetadataObjectsDelegate:self queue:dispatch_get_main_queue()];
    self.captureMetadataOutput.metadataObjectTypes = @[ AVMetadataObjectTypeUPCECode,AVMetadataObjectTypeEAN8Code, AVMetadataObjectTypeEAN13Code];
    self.previewLayer = [AVCaptureVideoPreviewLayer layerWithSession:self.captureSession];
    self.previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
    self.previewLayer.frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height);
    
    AVCaptureConnection *con = self.previewLayer.connection;
    con.videoOrientation = AVCaptureVideoOrientationPortrait;
    
    [self.view.layer insertSublayer:self.previewLayer atIndex:0];
}

- (IBAction)cancel:(id)sender {
    if(self.captureSession.isRunning)
        [self.captureSession stopRunning];
    [self dismissViewControllerAnimated:YES completion:NULL];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    if(!self.captureSession.isRunning)
       [self.captureSession startRunning];
    [self.view bringSubviewToFront:self.imageView];
    [self.view bringSubviewToFront:self.scanDescriptionLabel];
    [self.view bringSubviewToFront:self.upperImageView];
    CALayer *layer = [CALayer layer];
    CGRect bounds = self.scannerImageView.bounds;
    bounds.size.height = 10;
    layer.bounds = bounds;
    layer.position = CGPointMake(bounds.size.width / 2, bounds.size.height / 2 - 4);
    layer.backgroundColor = [UIColor colorWithRed:182 / 255.0f  green:0.f blue:.0f alpha:.3f].CGColor;
    layer.zPosition = -5;
    [self.scannerImageView.layer addSublayer:layer];
    
    [self.view bringSubviewToFront:self.scannerImageView];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"ProductWithPriceDetailTableViewController"]){
        ProductWithPriceDetailViewController *productWithPriceDetailTableViewController = (ProductWithPriceDetailViewController *)segue.destinationViewController;
        productWithPriceDetailTableViewController.productID = self.scannedBarcode;
    }
}

- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputMetadataObjects:(NSArray *)metadataObjects fromConnection:(AVCaptureConnection *)connection{
    for(AVMetadataObject *current in metadataObjects) {
        if([current isKindOfClass:[AVMetadataMachineReadableCodeObject class]]) {
            self.scannedBarcode = [((AVMetadataMachineReadableCodeObject *) current) stringValue];
            NSLog(@"Scanned Value : %@", self.scannedBarcode);
            if(self.captureSession.isRunning)
               [self.captureSession stopRunning];
            [self performSegueWithIdentifier:@"ProductWithPriceDetailTableViewController" sender:nil];
        }
    }
}


@end
