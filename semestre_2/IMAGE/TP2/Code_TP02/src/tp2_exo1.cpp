#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <iostream>

unsigned int mx = 0, my = 0;

//////////////////////////////////////////////////////////////////////////////////////////////////
void callBackKeyboard(int event, int x, int y, int flags, void* userdata)
{
   switch(event){
    case cv::EVENT_LBUTTONDOWN : 
      std::cout << "left button pressed at : " << x << ", " << y << std::endl;
      break;

    case cv::EVENT_RBUTTONDOWN : 
    case cv::EVENT_MBUTTONDOWN : 
    case cv::EVENT_MOUSEMOVE   : 
      break;
   }
}

cv::Mat extrapolation(const cv::Mat& image)
{
  cv::Mat result(image.rows * 3, image.cols * 3, CV_8UC3);
  std::cout << "image size : " << result.cols << " x " << result.rows << std::endl;
  const int x0 = image.rows, y0 = image.cols;

  for(auto i = 0; i < image.rows; i++)
  {
	  for(auto j = 0; j < image.cols; j++)
	  {
		result.at<cv::Vec3b>(x0 + i, y0 + j) = image.at<cv::Vec3b>(i, j);
		result.at<cv::Vec3b>(x0 + i, j) = image.at<cv::Vec3b>(i, image.cols - j - 1);
		result.at<cv::Vec3b>(x0 + i, 2 * y0 + j) = image.at<cv::Vec3b>(i, image.cols - j - 1);
		result.at<cv::Vec3b>(i, y0 + j) = image.at<cv::Vec3b>(image.rows - i - 1, j);
		result.at<cv::Vec3b>(2 * x0 + i, y0 + j) = image.at<cv::Vec3b>(image.rows - i - 1, j);
		result.at<cv::Vec3b>(i, j) = image.at<cv::Vec3b>(image.rows - i - 1, image.cols - j - 1);
		result.at<cv::Vec3b>(2 * x0 + i, 2 * y0 + j) = image.at<cv::Vec3b>(image.rows - i - 1, image.cols - j - 1);
		result.at<cv::Vec3b>(i, 2 * y0 + j) = image.at<cv::Vec3b>(image.rows - i - 1, image.cols - j - 1);
		result.at<cv::Vec3b>(2 * x0 + i, j) = image.at<cv::Vec3b>(image.rows - i - 1, image.cols - j - 1);
	  }
  }

  return result;
}

cv::Mat rotate(cv::Mat image, int angle)
{
  cv::Mat temp;
  image.copyTo(temp);
  cv::Point2f center((image.cols - 1) / 2.0, (image.rows - 1) / 2.0);
  cv::Mat rotation;

  for(auto i = 0; i < 20; i++)
  {
    rotation = cv::getRotationMatrix2D(center, angle * i, 1.0);
    warpAffine(image, temp, rotation, image.size());
    cv::waitKey(500);
    cv::imshow("image", temp);
  }
  return temp;
}

cv::Mat zoom(cv::Mat image)
{
  cv::Mat temp;
  image.copyTo(temp);
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

  // setup a window
  cv::namedWindow("image", 1);
  cv::setMouseCallback("image", callBackKeyboard, NULL);

  auto res = extrapolation(image);
  image = rotate(image, 10);
  image = rotate(image, -10);

  // main loop
  bool loopOn = true;
  while(loopOn){

    // display the image
    cv::imshow("image", image);
    cv::imshow("image", res);

    // if esc button is pressed
    int key = cv::waitKey(500) % 256;
    if(key == 27 || key == 'q')
      loopOn = false;   
  }


  return 1;
}
