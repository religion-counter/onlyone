import time

from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException

# path in MacOS
EXTENSION_PATH = '/Users/[User Name]/Library/Application Support/Google' \
                 '/Chrome/Default/Extensions/[Metamask Extension Id]/10.2.2_0.crx'
opt = webdriver.ChromeOptions()
opt.add_extension(EXTENSION_PATH)

driver = webdriver.Chrome(
    executable_path='/Users/[User Name]/PycharmProjects/Selenium/chromedriver',
    options=opt)


def find_element(finder_function, arg):
    for i in range(10):
        try:
            return finder_function(arg)
        except NoSuchElementException:
            time.sleep(3)
    return None


driver.switch_to.window(driver.window_handles[0])

get_started_button = find_element(driver.find_element_by_xpath, '//button[text()="Get Started"]')
get_started_button.click()

create_button = find_element(driver.find_element_by_xpath, '//button[text()="Create a Wallet"]')
create_button.click()

find_element(driver.find_element_by_xpath, '//button[text()="I Agree"]').click()

find_element(driver.find_element_by_id, 'create-password').send_keys('11111111')
find_element(driver.find_element_by_id, 'confirm-password').send_keys('11111111')
find_element(driver.find_element_by_class_name, 'first-time-flow__checkbox').click()

find_element(driver.find_element_by_xpath, '//button[text()="Create"]').click()

find_element(driver.find_element_by_xpath, '//button[text()="Next"]').click()

find_element(driver.find_element_by_xpath, '//button[text()="Remind me later"]').click()

driver.execute_script("window.open('https://opensea.io/assets/"
                      "0x11450058d796b02eb53e65374be59cff65d3fe7f/8813');")

EXTENSION_ID = 'nkbihfbeogaeaoehlefnkodbefgpgknn'
time.sleep(9)

driver.switch_to.window(driver.window_handles[2])
i_elements = find_element(driver.find_elements_by_tag_name, 'i')

for el in i_elements:
    if el.text == 'favorite_border':
        el.click()
        break

time.sleep(3)

wallets = find_element(driver.find_elements_by_tag_name, 'span')

for wallet in wallets:
    if wallet.text == 'MetaMask':
        wallet.click()
        break

time.sleep(3)

driver.switch_to.window(driver.window_handles[3])

buttons_in_metamask = find_element(driver.find_elements_by_tag_name, 'button')

for button in buttons_in_metamask:
    if button.text == 'Next':
        button.click()
        break

buttons_in_metamask = find_element(driver.find_elements_by_tag_name, 'button')

for button in buttons_in_metamask:
    if button.text == 'Connect':
        button.click()
        break

# driver.get('chrome-extension://{}/popup.html'.format(EXTENSION_ID))

time.sleep(9)

driver.switch_to.window(driver.window_handles[2])

time.sleep(3)

i_elements = find_element(driver.find_elements_by_tag_name, 'i')

for el in i_elements:
    if el.text == 'favorite_border':
        el.click()
        break

time.sleep(3)

driver.switch_to.window(driver.window_handles[3])


time.sleep(3)

buttons_in_metamask = find_element(driver.find_elements_by_tag_name, 'button')

for button in buttons_in_metamask:
    if button.text == 'Sign':
        button.click()
        break

time.sleep(7)

for handle in driver.window_handles:
    driver.switch_to.window(handle)
    driver.close()

'''
Compressing the extension
In order to load the extension on our automated browser we will first need to compress the Metamask extension to a .crx file, here are the steps:

Install Metamask on your regular chrome
Navigate to chrome://extensions/
Click 'Pack extension' and enter the local path to the Metamask extension This will generate a .crx file that you can use to load as an extension on Chromium. Save the name of the folder where the extension is installed, this will be the 'Extension ID' that we will use later.

'''
