// eslint.config.js
import js from "@eslint/js";
import globals from "globals";
import tseslint from "typescript-eslint";
import vue from "eslint-plugin-vue";
import vueParser from "vue-eslint-parser";
import prettier from "eslint-config-prettier";

export default [
    // 忽略
    { ignores: ["dist", "node_modules", "coverage"] },

    // 官方 JS 推荐（对象 ✅）
    js.configs.recommended,

    // TypeScript 推荐（数组 -> 展开 ✅）
    ...tseslint.configs.recommended,

    // Vue Flat 推荐（数组 -> 展开 ✅）
    ...vue.configs["flat/recommended"],

    // .vue：外层用 vue-parser，script 内再交给 TS parser
    {
        files: ["**/*.vue"],
        languageOptions: {
            parser: vueParser,
            parserOptions: {
                parser: tseslint.parser,
                ecmaVersion: "latest",
                sourceType: "module",
                extraFileExtensions: [".vue"],
            },
            globals: { ...globals.browser, ...globals.node },
        },
    },

    // .ts / .tsx
    {
        files: ["**/*.{ts,tsx}"],
        languageOptions: {
            parser: tseslint.parser,
            ecmaVersion: "latest",
            sourceType: "module",
            globals: { ...globals.browser, ...globals.node },
        },
    },

    // 关掉和 Prettier 冲突的格式规则（对象 ✅）
    prettier,

    // 你的个性化规则
    {
        files: ["**/*.{ts,tsx,vue,js,jsx}"],
        rules: {
            "no-console": "off",
            "no-unused-vars": "off",
            "@typescript-eslint/no-unused-vars": [
                "warn",
                { argsIgnorePattern: "^_", varsIgnorePattern: "^_" },
            ],
            "vue/multi-word-component-names": "off",
        },
    },
];
