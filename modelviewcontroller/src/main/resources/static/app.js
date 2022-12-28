$(document).ready(function () {

    const height = $(window).height(), width = $(window).width();
    //..........Code for Word Cloud............

    //Store Compressed Data
    let words = [{"text": "Please Tweet something", "size": 1}];

    // Encapsulate the word cloud functionality
    function wordCloud(selector) {

        const fill = d3.scaleOrdinal(d3.schemeCategory10);

        //Construct the word cloud's SVG element
        const svg = d3.select(selector).append("svg")
            .attr("width", width)
            .attr("height", height)
            .append("g")
            .attr("transform", "translate(" + (width / 2) + "," + (height / 2) + ")");

        //Draw the word cloud
        function draw(words) {
            const cloud = svg.selectAll("g text")
                .data(words, function (d) {
                    return d.text;
                });

            //Entering words
            cloud.enter()
                .append("text")
                .style("font-family", "Impact")
                .style("fill", function (d, i) {
                    return fill(i);
                })
                .attr("text-anchor", "middle")
                .attr('font-size', 1)
                .text(function (d) {
                    return d.text;
                });

            //Entering and existing words
            cloud
                .transition()
                .duration(600)
                .style("font-size", function (d) {
                    return d.size + "px";
                })
                .attr("transform", function (d) {
                    return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                })
                .style("fill-opacity", 1);

            //Exiting words
            cloud.exit()
                .transition()
                .duration(200)
                .style('fill-opacity', 1e-6)
                .attr('font-size', 1)
                .remove();
        }

        function returnRotation() {
            const angle = [0, -90, -60, -45, -30, 0, 30, 45, 60, 90];
            const index = Math.floor(Math.random() * 10);
            return angle[index];
        }

        //Use the module pattern to encapsulate the visualisation code. We'll
        // expose only the parts that need to be public.
        return {

            //Recompute the word cloud for a new set of words. This method will
            // asynchronously call draw when the layout has been computed.
            //The outside world will need to call this function, so make it part
            // of the wordCloud return value.
            update: function (words) {

                const maxSize = d3.max(words, function (d) {
                    return d.size
                });
                //Define Pixel of Text
                const pixScale = d3.scaleLinear()
                    .domain([0, maxSize])
                    .range([10, 80]);

                d3.layout.cloud().size([(width - 50), (height - 20)])
                    .words(words)
                    .padding(5)
                    .rotate(function () {
                        return ~~(Math.random() * 2) * returnRotation();
                    })
                    .font("Impact")
                    .fontSize(function (d) {
                        return Math.floor(pixScale(d.size));
                    })
                    .on("end", draw)
                    .start();
            }
        }

    }

    async function showNewWords(vis) {

        let response = await axios.get('/api/tweetcount')
            .then()
            .catch(err => console.error(err))

        words = JSON.parse(JSON.stringify(response.data))
        if (words.length === 0) {
            words = [{"text": "Please Tweet something", "size": 1}];
        }

        vis.update(words);
    }

    //Create a new instance of the word cloud visualisation.
    const myWordCloud = wordCloud('body');

    showNewWords(myWordCloud).then();
    showNewWords(myWordCloud).then();


    try {
        const streamLocation = new EventSource('/api/tweetEvent');

        streamLocation.addEventListener('newTweet', function (event) {

            console.log(event.data)
            showNewWords(myWordCloud).then();
            showNewWords(myWordCloud).then();

        });

    } catch (err) {
        console.log("Error connecting to stream");
    }

    //Start cycling through the demo data
    setInterval(function () {
        showNewWords(myWordCloud).then();
    }, 10000);
});