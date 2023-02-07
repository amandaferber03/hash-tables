# Discussion
To determine which implementation is a better choice for my search engine, I modified the Alpha thresholds for
rehashing, the probing strategies used in the Open Addressing implementation, and the choice of whether to 
rehash in the Chaining implementation. The results of my experiments are shown in the tables below. Ultimately, 
I learned that the open addressing implementations were measurably more efficient with regard to time and space 
complexity. 

I chose to implement Open Addressing with an Alpha = 0.5 and linear probing because I thought this was a good 
balance between speed and space complexity. However, I originally thought that the bigger datasets may
challenge the linear probing aspect of my implementation due to the risk of primary clustering. In comparison, I 
choose to implement Chaining as an array of ArrayLists with an Alpha = 1.0. I choose ArrayLists over linked lists
simply due to the ease of use and helpful getter and setter methods in Java's ArrayList implementation. With these
initial implementations, I found that Open Addressing (OA) outperformed Chaining on every dataset. For example, in the 
largest dataset, random164.txt, the OA implementation had a speed of 138.863 ms/op and a space usage of 207240872 bytes,
where the Chaining implementation had a speed of 497.899 ms/op and a space usage of 274789456 bytes. From here on, 
I wanted to see if any values of alpha or probing methods would allow the OA implementation to exceed its initial 
efficiency level. I also wanted to see if there was any way of improving the Chaining implementation, maybe even to
help the Chaining implementation beat the efficiency of the OA implementation.

For OA, I tested alpha values of 0.35 and 0.75 to compare against the alpha value of 0.5. It seems that an alpha value
of 0.35 performs the best with the fastest speeds overall. I felt that the increases in space usage compared to 
Alpha = 0.5 was negligible. With probing strategies, I set a constant alpha of 0.5 and tested quadratic and 
double probing. I found that quadratic probing resulted in the fasted speed among the probing strategies for 
the largest dataset, random164.txt. With smaller datasets, quadratic probing did not lead due to the fact that certain
indices in the array get skipped as the indices probed become exponentially higher. Double probing seemed to be the
least efficient implementation in these experiments. Even though primary and secondary clustering is avoided, it 
may be that the probing sequences determined by the keys do not always guarantee low collision rates. 
Since, since the Alpha = 0.35 implementation yielded the fastest times and adequate space usages, 
I felt that a combination of lowering the alpha value and utilizing quadratic probing may prove to be beneficial. 
I tested this hypothesis and found this to be incorrect. The combination of an alpha value of 0.35 and the use
of quadratic probing was not synergistic: the speeds were slower than these two conditions working individually. 
Therefore, I deem Alpha = 0.35 to be the best OA implementation of those I tested.

Unfortunately, none of the modifications to the alpha values or rehashing approaches improved the efficiency of the
Chaining implementations by any means. I changed the alpha values to 0.8 and 0.5, and I even did an experiment where
Alpha remained 1.0 but no rehashing was employed. It should be noted that this test took 1 hour and 37 minutes to run!
With each decrease of alpha, the chaining implementations became slower, possibly due to the clustering of 
data points in few auxiliary data structures within the hashmap. Therefore, I deem OA to be the better implementation.


Open Addressing: (Initial/Baseline)
File Name          Score            Units
apache.txt          138.863         ms/op
apache.txt          98347368        bytes
jhu.txt             0.161           ms/op
jhu.txt             28159188        bytes
joanne.txt          0.064           ms/op
joanne.txt          25301164        bytes
newegg.txt          71.728          ms/op
newegg.txt          77865112        bytes
random164.txt       422.451         ms/op
random164.txt       207240872       bytes
urls.txt            0.022           ms/op
urls.txt            29016444        bytes

Open Addressing: Linear Probing, Alpha = 0.35
apache.txt          139.735         ms/op
apache.txt          102877056       bytes
jhu.txt             0.164           ms/op
jhu.txt             26880344        bytes
joanne.txt          0.065           ms/op
joanne.txt          24823016        bytes
newegg.txt          71.266          ms/op
newegg.txt          79541808        bytes
random164.txt       415.036         ms/op
random164.txt       242874904       bytes
urls.txt            0.022           ms/op
urls.txt            28207388        bytes

Open Addressing: Linear Probing, Alpha = 0.75
apache.txt          155.030         ms/op
apache.txt          96190956        bytes
jhu.txt             0.166           ms/op
jhu.txt             26824588        bytes
joanne.txt          0.067           ms/op
joanne.txt          25065128        bytes
newegg.txt          74.792          ms/op
newegg.txt          86201324        bytes
random164.txt       430.321         ms/op
random164.txt       281589984       bytes
urls.txt            0.022           ms/op
urls.txt            28667520        bytes

Open Addressing: Quadratic Probing, Alpha = 0.5
apache.txt          139.798         ms/op
apache.txt          98332412        bytes
jhu.txt             0.161           ms/op
jhu.txt             26832792        bytes
joanne.txt          0.063           ms/op
joanne.txt          26039704        bytes
newegg.txt          72.366          ms/op
newegg.txt          75857716        bytes
random164.txt       415.726         ms/op
random164.txt       283306126       bytes
urls.txt            0.022           ms/op
urls.txt            28560388        bytes

0pen Addressing: Double Probing, Alpha = 0.5
apache.txt          143.634         ms/op
apache.txt          96515680        bytes
jhu.txt             0.161           ms/op
jhu.txt             27445792        bytes
joanne.txt          0.067           ms/op
joanne.txt          24968028        bytes
newegg.txt          71.967          ms/op
newegg.txt          76505752        bytes
random164.txt       443.609         ms/op
random164.txt       304308252       bytes
urls.txt            0.022           ms/op
urls.txt            28685456        bytes

Chaining: Alpha = 1.0 (Initial/Baseline)
apache.txt          151.516         ms/op
apache.txt          107326336       bytes
jhu.txt             0.176           ms/op
jhu.txt             269787490       bytes
joanne.txt          0.071           ms/op
joanne.txt          24714344        bytes
newegg.txt          75.396          ms/op
newegg.txt          81864836        bytes
random164.txt       497.899         ms/op
random164.txt       274789456       bytes
urls.txt            0.022           ms/op
urls.txt            28321028        bytes

Chaining: Alpha = 0.8
apache.txt          151.144         ms/op
apache.txt          103602708       bytes
jhu.txt             0.172           ms/op
jhu.txt             26821844        bytes
joanne.txt          0.070           ms/op
joanne.txt          25106772        bytes
newegg.txt          78.110          ms/op
newegg.txt          82118340        bytes
random164.txt       526.983         ms/op
random164.txt       278475564       bytes
urls.txt            0.022           ms/op
urls.txt            28549736        bytes

Chaining: Alpha = 0.5
apache.txt          152.103         ms/op
apache.txt          111932976       bytes
jhu.txt             0.172           ms/op
jhu.txt             27026868        bytes
joanne.txt          0.071           ms/op
joanne.txt          24329816        bytes
newegg.txt          74.083          ms/op
newegg.txt          65196872        bytes
random164.txt       599.570         ms/op
random164.txt       349346280       bytes
urls.txt            0.022           ms/op
urls.txt            28355464        bytes

Chaining: No Rehashing, Alpha = 1.0
apache.txt          4733.719        ms/op
apache.txt          107939600       bytes
jhu.txt             0.205           ms/op
jhu.txt             24660772        bytes
joanne.txt          0.090           ms/op
joanne.txt          23564640        bytes
newegg.txt          2957.833        ms/op
newegg.txt          104430456       bytes
random164.txt       908790.625      ms/op
random164.txt       143694072       bytes
urls.txt            0.022           ms/op
urls.txt            28548036        bytes