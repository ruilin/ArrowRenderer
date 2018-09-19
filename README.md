# ArrowRenderer
曲线箭头的绘制方法

<img width="300" height="534" src="https://github.com/ruilin/ArrowRenderer/blob/master/example.png"/>

ArrowRenderer提供一种基于贝塞尔曲线实现绘制曲线箭头的方法，同时支持任意角度的直线箭头绘制。

本案例中，在Android平台上实现，通过手指在屏幕中任意滑动，程序会根据滑动的轨迹画出相应的曲线箭头。
同时计算曲线和箭头方向的算法可以适用于其它平台。

#### 核心类：

* CurveLineRenderer：封装了曲线的绘制方法

* ArrowRenderer：继承自CurveLineRenderer，封装了曲线箭头的绘制方法

#### 接口调用流程：

* 调用 resetPoints(ArrayList<float[]> points) 传入像素坐标即可画出相应的曲线，如果需要画直线，仅需要传入两个点即可。

* 在 View.draw(Canvas canvas) 中调用 ArrowRenderer.draw(Canvas canvas)
