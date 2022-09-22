var path = require('path');

module.exports = {
    entry: './notification.js',
    devtool: "inline-source-map",
    cache: true,
    mode: 'development',
    output: {
        path: __dirname,
        filename: '../modelviewcontroller/src/main/resources/static/built/bundle.js',
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, './'),
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
            }
        ]
    }
};