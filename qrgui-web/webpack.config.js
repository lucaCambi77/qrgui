const webpack = require('webpack');
const path = require('path');

module.exports = (env) => {
    return {
        entry: "./main.ts",
        output: {
            filename: "bundle.js"
        },
        resolve: {
            // Add '.ts' and '.tsx' as a resolvable extension.
            extensions: [".webpack.js", ".web.js", ".ts", ".tsx", ".js"]
        },
        module: {
            rules: [
                // all files with a '.ts' or '.tsx' extension will be handled by 'ts-loader'
                { test: /\.tsx?$/, loader: "ts-loader" },
            ]
        },
        devtool: 'source-map',
        plugins: [
            new webpack.ProvidePlugin({
                jQuery: 'jquery',
                $: 'jquery',
                jquery: 'jquery'
            }),
            new webpack.DefinePlugin({
                'process.env.NODE_ENV': (null != env.prod && env.prod == true) ? '' : JSON.stringify('http://localhost:8080')
            })
        ],
        devServer: {
            // Can be omitted unless you are using 'docker' 
            host: '0.0.0.0',
            // This is where webpack-dev-server serves your bundle
            // which is created in memory.
            // To use the in-memory bundle,
            // your <script> 'src' should point to the bundle
            // prefixed with the 'publicPath', e.g.:
            //   <script src='http://localhost:9001/assets/bundle.js'>
            //   </script>
            publicPath: '/assets/',
            // The local filesystem directory where static html files
            // should be placed.
            // Put your main static html page containing the <script> tag
            // here to enjoy 'live-reloading'
            // E.g., if 'contentBase' is '../views', you can
            // put 'index.html' in '../views/main/index.html', and
            // it will be available at the url:
            //   https://localhost:9001/main/index.html
            contentBase: path.resolve(__dirname),
            // 'Live-reloading' happens when you make changes to code
            // dependency pointed to by 'entry' parameter explained earlier.
            // To make live-reloading happen even when changes are made
            // to the static html pages in 'contentBase', add 
            // 'watchContentBase'
            watchContentBase: true,
            compress: true,
            port: 8081
          },
    }
};
