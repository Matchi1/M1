#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <iostream>
#include <list>

cv::Mat moyenneur(int N) {
    cv::Mat h(N, N, CV_32F, 1./(N * N));
    return h;
}

cv::Vec3b median(int size, const cv::Mat src, int x, int y) {
    auto N = size * size;
    std::list<cv::Vec3b> ll;
    auto base_x = x - size / 2;
    auto base_y = y - size / 2;
    for(auto i = 0; i < size; i++) {
        for(auto j = 0; j < size; j++) {
            int tmp_x = std::max(i + base_x, 0), tmp_y = std::max(j + base_y, 0);
            tmp_x = std::min(tmp_x, src.rows - 1);
            tmp_y = std::min(tmp_y, src.cols - 1);
            ll.emplace_back(src.at<cv::Vec3b>(tmp_x, tmp_y));
        }
    }
    ll = std::sort(ll);
    return ll.at(N / 2 + 1);

}

cv::Vec3b apply_filter(int size, const cv::Mat filter, const cv::Mat src, int x, int y) {
    auto base_x = x - size / 2;
    auto base_y = y - size / 2;
    unsigned char r = 0, g = 0, b = 0;
    for(auto i = 0; i < size; i++) {
        for(auto j = 0; j < size; j++) {
            int tmp_x = std::max(i + base_x, 0), tmp_y = std::max(j + base_y, 0);
            tmp_x = std::min(tmp_x, src.rows - 1);
            tmp_y = std::min(tmp_y, src.cols - 1);
            r += src.at<cv::Vec3b>(tmp_x, tmp_y)[0] * filter.at<float>(i, j);
            g += src.at<cv::Vec3b>(tmp_x, tmp_y)[1] * filter.at<float>(i, j);
            b += src.at<cv::Vec3b>(tmp_x, tmp_y)[2] * filter.at<float>(i, j);
        }
    }
    return cv::Vec3b(r, g, b);
}

void Filtrage(const cv::Mat& src, cv::Mat& dst, int filtreType, int argument) {
    auto h = moyenneur(argument);
    for(auto x = 0; x < src.rows; x++) {
        for(auto y = 0; y < src.cols; y++) {
            switch(filtreType) {
                case 0:
                    dst.at<cv::Vec3b>(x, y) = apply_filter(argument, h, src, x, y);
                    break;
                case 1:
                    dst.at<cv::Vec3b>(x, y) = median(argument, src, x, y);
                    break;
            }
        }
    }
}

int main(int argc, char ** argv) {
  // check arguments
  if(argc != 2){
    std::cout << "usage: " << argv[0] << " image" << std::endl;
    return -1;
  }

  // load the input image
  std::cout << "load image ..." << std::endl;
  cv::Mat image = cv::imread(argv[1]);
  cv::Mat dst = cv::imread(argv[1]);
  if(image.empty()){
    std::cout << "error loading " << argv[1] << std::endl;
    return -1;
  }
  std::cout << "image size : " << image.cols << " x " << image.rows << std::endl;

  // setup a window
  cv::namedWindow("image", 1);
    Filtrage(image, dst, 0, 5);
    cv::imshow("image", image);
    cv::imshow("dst", dst);
  std::cout << "appuyer sur une touche ..." << std::endl;
  cv::waitKey();
  return 1;
}