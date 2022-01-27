#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <iostream>





//////////////////////////////////////////////////////////////////////////////////////////////////

void color_print(cv::Mat& image)
{
  // RGB value of pixel at position (50,100)
  std::cout << "B:" << (int)image.at<cv::Vec3b>(50, 100)[0] << std::endl;
  std::cout << "G:" << (int)image.at<cv::Vec3b>(50, 100)[1] << std::endl;
  std::cout << "R:" << (int)image.at<cv::Vec3b>(50, 100)[2] << std::endl;
}


void color_pixel(cv::Mat& image)
{
  // Change color of pixel at position (10,20)
  image.at<cv::Vec3b>(10,20)[2] = 255;
  image.at<cv::Vec3b>(10,20)[1] = 0;
  image.at<cv::Vec3b>(10,20)[0] = 0;
}

void color_line(cv::Mat& image)
{
  // Color line 42 with red
  for(int i = 0; i < image.cols; i++)
  {
      image.at<cv::Vec3b>(42,i)[2] = 255;
      image.at<cv::Vec3b>(42,i)[1] = 0;
      image.at<cv::Vec3b>(42,i)[0] = 0;
  }
}

void color_shift50(cv::Mat& image)
{
  // Color shift
  for(int i = 0; i < image.rows; i++)
  {
      for(int j = 0; j < image.cols; j++)
      {
          image.at<cv::Vec3b>(i,j)[2] += 50;
          image.at<cv::Vec3b>(i,j)[1] += 50;
          image.at<cv::Vec3b>(i,j)[0] += 50;
      }
  }
}

int clamp(int value)
{
    return std::min(std::max(value, 0), 255);
}

void color_shift50_clamp(cv::Mat& image)
{
  // Color shift
  for(int i = 0; i < image.rows; i++)
  {
      for(int j = 0; j < image.cols; j++)
      {
          auto pixel = image.at<cv::Vec3b>(i,j);
          int a = pixel[0] + 50;
          int b = pixel[1] + 50;
          int c = pixel[2] + 50;

          image.at<cv::Vec3b>(i,j)[0] = clamp(a);
          image.at<cv::Vec3b>(i,j)[1] = clamp(b);
          image.at<cv::Vec3b>(i,j)[2] = clamp(c);
      }
  }
}

void color_negative(cv::Mat& image)
{
  // Color shift
  for(int i = 0; i < image.rows; i++)
  {
      for(int j = 0; j < image.cols; j++)
      {
          auto pixel = image.at<cv::Vec3b>(i,j);
          int a = 255 - pixel[0];
          int b = 255 - pixel[1];
          int c = 255 - pixel[2];

          image.at<cv::Vec3b>(i,j)[0] = a;
          image.at<cv::Vec3b>(i,j)[1] = b;
          image.at<cv::Vec3b>(i,j)[2] = c;
      }
  }
}

void color_convert_BGR2GRAY(cv::Mat& image)
{
  // Color shift
  cv::cvtColor(image, image, cv::COLOR_BGR2GRAY);
}

void color_BW(cv::Mat& image)
{
  cv::cvtColor(image, image, cv::COLOR_BGR2GRAY);
  for(int i = 0; i < image.rows; i++)
  {
      for(int j = 0; j < image.cols; j++)
      {
          int value = image.at<unsigned char>(i,j);
          if(value < 128)
          {
            image.at<unsigned char>(i,j) = 0;
          }
          else
          {
            image.at<unsigned char>(i,j) = 255;
          }
      }
  }
}

void green_square(cv::Mat& image)
{
    int length = 50;
    int height = 50;
    int x0 = (image.rows - length) / 2;
    int x1 = x0 + length;
    int y0 = (image.cols - height) / 2;
    int y1 = y0 + height;
  for(int i = x0; i < x1; i++)
  {
      for(int j = y0; j < y1; j++)
      {
          image.at<cv::Vec3b>(i,j)[0] = 0;
          image.at<cv::Vec3b>(i,j)[1] = 255;
          image.at<cv::Vec3b>(i,j)[2] = 0;
      }
  }
}

void gradient(cv::Mat& image)
{
    int length = 50;
    int height = 50;
    int x0 = (image.rows - length) / 2;
    int x1 = x0 + length;
    int y0 = (image.cols - height) / 2;
    int y1 = y0 + height;
    int nb_pixel = length * height;
    int index = nb_pixel;
  for(int i = x0; i < x1; i++)
  {
      for(int j = y0; j < y1; j++)
      {
          image.at<cv::Vec3b>(i,j)[0] = index * 100 / nb_pixel;
          image.at<cv::Vec3b>(i,j)[1] = index * 100 / nb_pixel;
          image.at<cv::Vec3b>(i,j)[2] = index * 100 / nb_pixel;
          index--;
      }
  }
}

void circle(cv::Mat& image)
{
    cv::Point center(image.cols / 2, image.rows / 2);
    cv::circle(image,
            center,
            30,
            cv::Scalar( 0, 255, 0 ),
            cv::FILLED,
            cv::LINE_8 );
}

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

  circle(image);

  // display an image
  cv::imshow("une image", image);
  std::cout << "appuyer sur une touche ..." << std::endl;
  cv::waitKey();

  // save the image
  cv::imwrite("output/tp1ex1.jpg",image);

  return 1;
}
