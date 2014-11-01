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
//@property (nonatomic, strong) ZXCapture *capture;
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
    [self.captureSession startRunning];
    // Do any additional setup after loading the view.
//    self.capture = [[ZXCapture alloc] init];
//    self.capture.camera = self.capture.back;
//    self.capture.focusMode = AVCaptureFocusModeContinuousAutoFocus;
//    self.capture.rotation = 90.0f;
//    
//    self.capture.layer.frame = self.view.bounds;
//    [self.view.layer addSublayer:self.capture.layer];
//    
//    //Giving hints to make it faster
//    [self.capture.hints addPossibleFormat:kBarcodeFormatUPCA];
//    [self.capture.hints addPossibleFormat:kBarcodeFormatUPCE];
//    [self.capture.hints addPossibleFormat:kBarcodeFormatUPCEANExtension];
//    [self.capture.hints addPossibleFormat:kBarcodeFormatEan8];
//    [self.capture.hints addPossibleFormat:kBarcodeFormatEan13];
//    NSLog(@"numberOfPossibleFormats: %zd",self.capture.hints.numberOfPossibleFormats);
    
    [self.scanDescriptionLabel setText:@"Hold up to a barcode to scan"];
    [self.view bringSubviewToFront:self.scanDescriptionLabel];
}

- (void)setupScanner{
    
    self.captureDevice = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];


    self.captureDeviceInput = [AVCaptureDeviceInput deviceInputWithDevice:self.captureDevice error:nil];

    
    self.captureSession = [[AVCaptureSession alloc] init];
    
    self.captureMetadataOutput = [[AVCaptureMetadataOutput alloc] init];
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


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)cancel:(id)sender {
//    [self.capture.layer removeFromSuperlayer];
//    [self.capture stop];
    if(self.captureSession.isRunning)
        [self.captureSession stopRunning];
    [self dismissViewControllerAnimated:YES completion:NULL];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    NSLog(@"viewWillAppear");
    if(!self.captureSession.isRunning)
       [self.captureSession startRunning];
//    [self.capture start];
//    self.capture.delegate = self;
//    self.capture.layer.frame = self.view.bounds;

//    [self.view.layer addSublayer:self.capture.layer];
//    [self.scanDescriptionLabel setText:@"Hold up to a barcode to scan"];
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
    
//    self.capture.scanRect = CGRectMake(0, CGRectGetMaxY(self.upperImageView.frame), self.view.frame.size.width,  CGRectGetMinY(self.imageView.frame) -CGRectGetMaxY(self.upperImageView.frame) );
//    NSLog(@"ScanRect frame : %@", NSStringFromCGRect(self.capture.scanRect));
    
//    self.scannerImageView.layer.shadowColor = [UIColor colorWithRed:182 / 255.0f  green:0.f blue:.0f alpha:1.f].CGColor;
//    self.scannerImageView.layer.shadowOffset = CGSizeMake(0, -2);
//    self.scannerImageView.layer.shadowOpacity = .7f;
//    self.scannerImageView.layer.shadowRadius = 1.0;
//    self.scannerImageView.clipsToBounds = NO;
    [self.view bringSubviewToFront:self.scannerImageView];
    
//    CGAffineTransform captureSizeTransform = CGAffineTransformMakeScale(320 / self.view.frame.size.width, 480 / self.view.frame.size.height);
//    self.capture.scanRect = CGRectApplyAffineTransform(self.scanRectView.frame, captureSizeTransform);
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

//-(void)captureResult:(ZXCapture *)capture result:(ZXResult *)result{
//    if (!result) return;
//    
//    self.capture.delegate = nil;
//    // We got a result. Display information about the result onscreen.
//    NSString *formatString = [self barcodeFormatToString:result.barcodeFormat];
//    NSString *display = [NSString stringWithFormat:@"Scanned!\n\nFormat: %@\n\nContents:\n%@", formatString, result.text];
//    NSLog(@"Scanned!\n\nFormat: %@\n\nContents:\n%@", formatString, result.text);
//    [self.scanDescriptionLabel performSelectorOnMainThread:@selector(setText:) withObject:display waitUntilDone:YES];
//    self.scannedBarcode = result.text;
//    [self performSegueWithIdentifier:@"ProductWithPriceDetailTableViewController" sender:nil];
//}


- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"ProductWithPriceDetailTableViewController"]){
        ProductWithPriceDetailViewController *productWithPriceDetailTableViewController = (ProductWithPriceDetailViewController *)segue.destinationViewController;
        productWithPriceDetailTableViewController.productID = 11000036;
    }
}



- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputMetadataObjects:(NSArray *)metadataObjects fromConnection:(AVCaptureConnection *)connection{
    for(AVMetadataObject *current in metadataObjects) {
        if([current isKindOfClass:[AVMetadataMachineReadableCodeObject class]]) {
            NSString *scannedValue = [((AVMetadataMachineReadableCodeObject *) current) stringValue];
            NSLog(@"Scanned Value : %@", scannedValue);
            if(self.captureSession.isRunning)
               [self.captureSession stopRunning];
            [self performSegueWithIdentifier:@"ProductWithPriceDetailTableViewController" sender:nil];
        }
    }
}

- (NSString *)barcodeFormatToString:(ZXBarcodeFormat)format {
    switch (format) {
        case kBarcodeFormatAztec:
            return @"Aztec";
            
        case kBarcodeFormatCodabar:
            return @"CODABAR";
            
        case kBarcodeFormatCode39:
            return @"Code 39";
            
        case kBarcodeFormatCode93:
            return @"Code 93";
            
        case kBarcodeFormatCode128:
            return @"Code 128";
            
        case kBarcodeFormatDataMatrix:
            return @"Data Matrix";
            
        case kBarcodeFormatEan8:
            return @"EAN-8";
            
        case kBarcodeFormatEan13:
            return @"EAN-13";
            
        case kBarcodeFormatITF:
            return @"ITF";
            
        case kBarcodeFormatPDF417:
            return @"PDF417";
            
        case kBarcodeFormatQRCode:
            return @"QR Code";
            
        case kBarcodeFormatRSS14:
            return @"RSS 14";
            
        case kBarcodeFormatRSSExpanded:
            return @"RSS Expanded";
            
        case kBarcodeFormatUPCA:
            return @"UPCA";
            
        case kBarcodeFormatUPCE:
            return @"UPCE";
            
        case kBarcodeFormatUPCEANExtension:
            return @"UPC/EAN extension";
            
        default:
            return @"Unknown";
    }
}


@end
