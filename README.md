# PagingRecyclerView (AndroidX)
[![JitPack](https://jitpack.io/v/derong218/PagingRecyclerView.svg)](https://jitpack.io/#derong218/PagingRecyclerView)
[![License](https://img.shields.io/github/license/derong218/PagingRecyclerView.svg)](https://github.com/derong218/PagingRecyclerView/blob/master/LICENSE)

横向分页 RecyclerView和页码指示器，不需要继承指定Adapter

## 配置
### Gradle 依赖:
在项目根目录 `build.gradle` 文件增加

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
在你的模块 `build.gradle` 文件增加
```gradle
dependencies {
     implementation 'com.github.derong218:PagingRecyclerView:1.0.0'
}
```

## 使用:

### 分页RecyclerView
```xml
 <com.arx.pagingrecyclerview.PagingRecyclerView
        android:id="@+id/rv_test"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="horizontal"
        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
        app:pageSize="10"
        tools:itemCount="40"
        tools:listitem="@layout/item">
 </com.arx.pagingrecyclerview.PagingRecyclerView>
```

```java
final PagingRecyclerView rvTest = findViewById(R.id.rv_test);
rvTest.setAdapter(adapter);
//监听页数和页码改变
rvTest.addOnPageChangeListener(new PagingRecyclerView.OnPageChangeListener() {
            /**
             *
             * @param page 当前页，0：第一页
             */
            @Override
            public void onPageSelected(int page) {
                tvPage.setText(page + 1 + "");
            }

            /**
             * @param pageCount 页数
             */
            @Override
            public void onPageCount(int pageCount) {
                tvPageCount.setText("" + pageCount);
            }
        });
```
### 页码指示器

```xml
 <com.arx.pagingrecyclerview.IndicatorView
        android:id="@+id/indicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:indicatorColor="@color/colorPrimary"
        app:indicatorHeight="10dp"
        app:indicatorPaddingEnd="4dp"
        app:indicatorPaddingStart="4dp"
        app:indicatorSelectedColor="@color/colorAccent"
        app:indicatorWidth="10dp">
```

矩形设置indicatorRadius为0dp，默认为圆形
```xml
app:indicatorRadius="0dp"
```

关联PagingRecyclerView
```java
indicatorView.setupWithPagingRecyclerView(recyclerView);
```



## License:

```
MIT License

Copyright (c) 2019 Arx

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```


