### Tips

After comparing index.weex.js generated by weex-hackernews and app.weex.js generated by my own project.
I found the diff finally:

```
// app.weex.js's banner:
/*!
 * // { "framework": "Vue" }
 *
 */

// index.weex.js's banner:
// { "framework": "Vue" }
```

We can see that our banner is commented again.
Weex uses `// { "framework": "Vue" }` to adjust runtime, so webpack.BannerPlugin need raw property to be true.