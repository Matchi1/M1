#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#include <iostream>
#include <cmath>

#include "fourierTransform.hpp"

cv::Mat sobelXFourierKernel(const int width, const int height)
{
  int base_x = (width - 3)/ 2;
  int base_y = (height - 3)/ 2;
  auto matrix = cv::Mat(height, width, CV_32F, cv::Scalar(0.0));
  matrix.at<float>(base_x, base_y) = -1;
  matrix.at<float>(base_x, base_y + 1) = -2;
  matrix.at<float>(base_x, base_y + 2) = -1;
  matrix.at<float>(base_x + 2, base_y) = 1;
  matrix.at<float>(base_x + 2, base_y + 1) = 2;
  matrix.at<float>(base_x + 2, base_y + 2) = 1;
  return matrix;
}

void product(cv::Mat &image, cv::Mat &filter)
{
  for(auto i = 0; i < image.rows; i++)
  {
    for(auto j = 0; j < image.cols; j++)
    {
      image.at<float>(i, j) *= filter.at<float>(i, j);
    }
  }
}

cv::Mat LaplacienKernel(const int width, const int height)
{
  int base_x = (width - 3)/ 2;
  int base_y = (height - 3)/ 2;
  auto matrix = cv::Mat(height, width, CV_32F, cv::Scalar(0.0));
  matrix.at<float>(base_x, base_y + 1) = 1;
  matrix.at<float>(base_x + 1, base_y) = 1;
  matrix.at<float>(base_x + 1, base_y + 1) = -4;
  matrix.at<float>(base_x + 1, base_y + 2) = 1;
  matrix.at<float>(base_x + 2, base_y + 1) = 1;
  return matrix;
}

cv::Mat sobelYFourierKernel(const int width, const int height)
{
  int base_x = (width - 3)/ 2;
  int base_y = (height - 3)/ 2;
  auto matrix = cv::Mat(height, width, CV_32F, cv::Scalar(0.0));
  matrix.at<float>(base_x, base_y) = -1;
  matrix.at<float>(base_x + 1, base_y) = -2;
  matrix.at<float>(base_x + 2, base_y) = -1;
  matrix.at<float>(base_x, base_y + 2) = 1;
  matrix.at<float>(base_x + 1, base_y + 2) = 2;
  matrix.at<float>(base_x + 2, base_y + 2) = 1;
  return matrix;
}

cv::Mat RKernel(const int width, const int height)
{
  int base_x = (width - 3)/ 2;
  int base_y = (height - 3)/ 2;
  auto matrix = cv::Mat(height, width, CV_32F, cv::Scalar(0.0));
  matrix.at<float>(base_x, base_y + 1) = -1;
  matrix.at<float>(base_x + 1, base_y) = -1;
  matrix.at<float>(base_x + 1, base_y + 1) = 5;
  matrix.at<float>(base_x + 1, base_y + 2) = -1;
  matrix.at<float>(base_x + 2, base_y + 1) = -1;
  return matrix;
}

cv::Mat gaussianFourierKernel(const int width, const int height, const double sigma)
{
  cv::Mat kernel(height, width, CV_32F, cv::Scalar(0.0));
  int x = width/ 2;
  int y = height/ 2;
  for(auto i = 0; i < width; i++) {
    for(auto j = 0; j < height; j++) {
      auto g = (1 / (2 * M_PI * (sigma * sigma)) * exp(- (i * i + j * j) / (2 * sigma * sigma)));
      kernel.at<float>(i, j) = g;
    }
  }
  return kernel;
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
  if(image.empty()){
    std::cout << "error loading " << argv[1] << std::endl;
    return -1;
  }
  std::cout << "image size : " << image.cols << " x " << image.rows << std::endl;

  // convert to gray scale
  cv::cvtColor(image, image, cv::COLOR_RGB2GRAY);


  // discrete Fourier Transform
  cv::Mat imageFourierMagnitude, imageFourierPhase;
  discreteFourierTransform(image, imageFourierMagnitude, imageFourierPhase);

  // do someting here ....
  // 1.2
  //imageFourierMagnitude.at<float>(imageFourierMagnitude.rows / 2, -10 + imageFourierMagnitude.cols / 2) = 0.0;
  //imageFourierMagnitude.at<float>(imageFourierMagnitude.rows / 2, 10 + imageFourierMagnitude.cols / 2) = 0.0;

  // 2.2
  // removeRing(imageFourierMagnitude, 100, 1000);
  // 2.3
  // removeRing(imageFourierMagnitude, 0, 10);

  // 2.3
  // removeRing(imageFourierMagnitude, 0, 10);
  // removeRing(imageFourierMagnitude, 20, 1000);

  // 3.2
  // cv::Mat filterFourierMagnitude, filterFourierPhase;
  // auto filter = sobelXFourierKernel(imageFourierMagnitude.rows, imageFourierMagnitude.cols);
  // discreteFourierTransform(filter, filterFourierMagnitude, filterFourierPhase);

  //product(imageFourierMagnitude, filterFourierMagnitude);

  // inverse Fourier Transform
  //cv::Mat outputImage;
  //inverseDiscreteFourierTransform(imageFourierMagnitude, imageFourierPhase, outputImage, CV_8U);

  // 3.3
  //cv::Mat filterFourierMagnitude, filterFourierPhase;
  // auto filter = sobelYFourierKernel(imageFourierMagnitude.cols, imageFourierMagnitude.rows);
  // discreteFourierTransform(filter, filterFourierMagnitude, filterFourierPhase);

  // product(imageFourierMagnitude, filterFourierMagnitude);

  // // inverse Fourier Transform
  // cv::Mat outputImage;
  // inverseDiscreteFourierTransform(imageFourierMagnitude, imageFourierPhase, outputImage, CV_8U);

  // 3.4
  // cv::Mat filterFourierMagnitude, filterFourierPhase;
  // auto filter = LaplacienKernel(imageFourierMagnitude.cols, imageFourierMagnitude.rows);
  // discreteFourierTransform(filter, filterFourierMagnitude, filterFourierPhase);

  // product(imageFourierMagnitude, filterFourierMagnitude);

  // // inverse Fourier Transform
  // cv::Mat outputImage;
  // inverseDiscreteFourierTransform(imageFourierMagnitude, imageFourierPhase, outputImage, CV_8U);

  // 3.5
  // cv::Mat filterFourierMagnitude, filterFourierPhase;
  // auto filter = RKernel(imageFourierMagnitude.cols, imageFourierMagnitude.rows);
  // discreteFourierTransform(filter, filterFourierMagnitude, filterFourierPhase);

  // product(imageFourierMagnitude, filterFourierMagnitude);

  // // inverse Fourier Transform
  // cv::Mat outputImage;
  // inverseDiscreteFourierTransform(imageFourierMagnitude, imageFourierPhase, outputImage, CV_8U);

  // 3.6
  cv::Mat filterFourierMagnitude, filterFourierPhase;
  auto filter = gaussianFourierKernel(imageFourierMagnitude.cols, imageFourierMagnitude.rows, 0.4);
  discreteFourierTransform(filter, filterFourierMagnitude, filterFourierPhase);

  product(imageFourierMagnitude, filterFourierMagnitude);

  // inverse Fourier Transform
  cv::Mat outputImage;
  inverseDiscreteFourierTransform(imageFourierMagnitude, imageFourierPhase, outputImage, CV_8U);

  // display everything
  cv::namedWindow("Input image");
  cv::namedWindow("Magnitude");
  cv::namedWindow("Output image");

  cv::moveWindow("Input image",100, 50);
  cv::moveWindow("Magnitude",700, 50);
  cv::moveWindow("Output image",100, 400);

  cv::imshow("Input image", image);
  cv::imshow("Magnitude", fourierMagnitudeToDisplay(imageFourierMagnitude));
  cv::imshow("Output image", outputImage);
  cv::waitKey();
 
  // save the images
  cv::imwrite("output/inputImage.jpg",image);
  cv::imwrite("output/magnitude.png", fourierMagnitudeToDisplay(imageFourierMagnitude));
  cv::imwrite("output/pahse.png", fourierPhaseToDisplay(imageFourierPhase));
  cv::imwrite("output/filteredImage.png",outputImage);

  return 0;
}
