#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#include <iostream>
#include <cmath>

void dilation(int size, cv::Mat dilation_dst) {
  int dilation_type = cv::MORPH_RECT;
  auto element = cv::getStructuringElement( dilation_type,
                       cv::Size( 2*size + 1, 2*size+1 ),
                       cv::Point( size, size ) );

  dilate( dilation_dst, dilation_dst, element );
}

void erosion(int size, cv::Mat erosion_dst) {
  int erosion_type = cv::MORPH_RECT;
  auto element = cv::getStructuringElement( erosion_type,
                       cv::Size( 2*size + 1, 2*size+1 ),
                       cv::Point( size, size ) );

  erode( erosion_dst, erosion_dst, element );
}

void open(int size, cv::Mat dst) {
  int dst_type = cv::MORPH_RECT;
  auto element = cv::getStructuringElement( dst_type,
                       cv::Size( 2*size + 1, 2*size+1 ),
                       cv::Point( size, size ) );

  morphologyEx( dst, dst, cv::MORPH_OPEN, element );
}

void close(int size, cv::Mat dst) {
  int dst_type = cv::MORPH_RECT;
  auto element = cv::getStructuringElement( dst_type,
                       cv::Size( 2*size + 1, 2*size+1 ),
                       cv::Point( size, size ) );

  morphologyEx( dst, dst, cv::MORPH_CLOSE, element );
}

void morphology(int size, cv::Mat dst) {
  int dst_type = cv::MORPH_RECT;
  auto element = cv::getStructuringElement( dst_type,
                       cv::Size( 2*size + 1, 2*size+1 ),
                       cv::Point( size, size ) );

  morphologyEx( dst, dst, 4, element );
}

void custom_dilation(int size, cv::Mat dst) {
  cv::Mat src = dst.clone();
  for(auto i = size; i < src.rows - size; i++) {
    for(auto j = size; j < src.cols - size; j++) {
      if(src.at<cv::Vec3b>(i, j) == cv::Vec3b(255, 255, 255)) {
        for(auto x = i - size; x < i + size; x++) {
          for(auto y = j - size; y < j + size; y++) {
            dst.at<cv::Vec3b>(x, y) = cv::Vec3b(255, 255, 255);
          }
        }
      }
    }
  }
}

void custom_erosion(int size, cv::Mat dst) {
  cv::Mat src = dst.clone();
  bool erode = false;
  for(auto i = size; i < src.rows - size; i++) {
    for(auto j = size; j < src.cols - size; j++) {
      for(auto x = i - size; x < i + size && !erode; x++) {
        for(auto y = j - size; y < j + size && !erode; y++) {
          if(src.at<cv::Vec3b>(x, y) == cv::Vec3b(0, 0, 0)) {
            erode = true;
          }
        }
      }
      if(erode) {
        dst.at<cv::Vec3b>(i, j) = cv::Vec3b(0, 0, 0);
      }
    }
  }
}

//////////////////////////////////////////////////////////////////////////////////////////////////
int main(int argc, char ** argv)
{
  // check arguments
  if(argc != 2){
    std::cout << "usage: " << argv[0] << " image" << std::endl;
    return -1;
  }

  // load the input image
  std::cout << "load image ..." << std::endl;
  cv::Mat image = cv::imread(argv[1]);
  cv::Mat dst = cv::imread(argv[1]);
  cv::Mat eroded = cv::imread(argv[1]);
  cv::Mat opened = cv::imread(argv[1]);
  cv::Mat closed = cv::imread(argv[1]);
  cv::Mat morp = cv::imread(argv[1]);
  cv::Mat e_potent = cv::imread(argv[1]);
  cv::Mat d_potent = cv::imread(argv[1]);
  cv::Mat c_dilation = cv::imread(argv[1]);
  cv::Mat c_erose = cv::imread(argv[1]);

  if(image.empty()){
    std::cout << "error loading " << argv[1] << std::endl;
    return -1;
  }
  std::cout << "image size : " << image.cols << " x " << image.rows << std::endl;

  dilation(5, dst);
  erosion(5, eroded);
  open(5, opened);
  close(5, closed);
  morphology(5, morp);
  open(5, e_potent);
  open(5, e_potent);
  close(5, d_potent);
  close(5, d_potent);
  custom_dilation(5, c_dilation);
  custom_erosion(5, c_erose);

  cv::namedWindow("Input image");
  cv::namedWindow("Output image");
  cv::namedWindow("Eroded image");
  cv::namedWindow("Opened image");
  cv::namedWindow("Closed image");
  cv::namedWindow("Morph image");
  cv::namedWindow("E potent image");
  cv::namedWindow("D potent image");
  cv::namedWindow("custom dilation image");
  cv::namedWindow("custom erose image");

  cv::moveWindow("Input image",100, 50);
  cv::moveWindow("Output image",100, 400);
  cv::moveWindow("Eroded image",500, 400);
  cv::moveWindow("Opened image",800, 400);
  cv::moveWindow("Closed image",1000, 400);
  cv::moveWindow("Morph image",1200, 400);
  cv::moveWindow("E potent image",800, 800);
  cv::moveWindow("D potent image",1000, 800);
  cv::moveWindow("custom dilation image",100, 1000);
  cv::moveWindow("custom erose image",100, 1000);

  cv::imshow("Input image", image);
  cv::imshow("Output image", dst);
  cv::imshow("Eroded image", eroded);
  cv::imshow("Opened image", opened);
  cv::imshow("Closed image", closed);
  cv::imshow("Morph image", morp);
  cv::imshow("E potent image", e_potent);
  cv::imshow("D potent image", d_potent);
  cv::imshow("custom dilation image", c_dilation);
  cv::imshow("custom erose image", c_dilation);
  cv::waitKey();
 
  // save the images
  cv::imwrite("output/inputImage.jpg",image);
  cv::imwrite("output/dilateImage.jpg",dst);
  cv::imwrite("output/erodeImage.jpg",eroded);
  cv::imwrite("output/openImage.jpg",opened);
  cv::imwrite("output/closeImage.jpg",closed);
  cv::imwrite("output/morpImage.jpg",morp);
  cv::imwrite("output/epotentImage.jpg",e_potent);
  cv::imwrite("output/dpotentImage.jpg",d_potent);
  cv::imwrite("output/cdilationImage.jpg",c_dilation);
  cv::imwrite("output/ceroseImage.jpg",c_erose);

  return 0;
}
