var webpack = require('webpack');

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
        ]
    }
};
