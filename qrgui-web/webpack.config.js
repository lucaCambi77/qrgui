const webpack = require('webpack')
const path = require('path')

module.exports = (env) => {
  return {
    entry: './main.ts',
    output: {
      filename: 'bundle.js'
    },
    resolve: {
      // Add '.ts' and '.tsx' as a resolvable extension.
      extensions: ['.webpack.js', '.web.js', '.ts', '.tsx', '.js']
    },
    module: {
      rules: [
        // all files with a '.ts' or '.tsx' extension will be handled by 'ts-loader'
        { test: /\.tsx?$/, loader: 'ts-loader' },
        {
          test: require.resolve("jquery"),
          loader: "expose-loader",
          options: {
            exposes: ["$", "jQuery"],
          },
        }
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
        'process.env.NODE_ENV': (env.prod != null && env.prod === true) ? '' : JSON.stringify('http://localhost:8080')
      })
    ],
    devServer: {
      // Can be omitted unless you are using 'docker'
      host: 'localhost',
      publicPath: '/assets/',
      contentBase: path.resolve(__dirname),
      watchContentBase: true,
      compress: true,
      port: 8083
    }
  }
}
