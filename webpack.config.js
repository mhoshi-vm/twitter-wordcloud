var path = require('path');

module.exports = {
    entry: './src/main/resources/static/notification.js',
    devtool: "inline-source-map",
    cache: true,
    mode: 'development',
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js',
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            },
            {
                test: /\.css$/i,
                use: ["style-loader", "css-loader"],
            },
            {
                test: /\.(woff(2)?|ttf|eot|svg)$/,
                type: 'asset/resource',
                generator: {
                    publicPath: './built/',
                    outputPath: './src/main/resources/static/built',
                    filename: '[name][ext]'
                },
            },
        ]
    }
};