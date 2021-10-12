module.exports = {
  root: true,
  extends: '@react-native-community',
  parser: '@typescript-eslint/parser',
  plugins: ['@typescript-eslint'],

  rules: {
    semi: ['error', 'never'],
    quotes: ['error', 'single', { avoidEscape: false }],

    'no-unused-vars': 'off',
    '@typescript-eslint/no-unused-vars': 'warn',

    'eslint-comments/no-unlimited-disable': 'off',
    'eslint-comments/no-unused-disable': 'off',
    'prettier/prettier': 0,
  },
}
