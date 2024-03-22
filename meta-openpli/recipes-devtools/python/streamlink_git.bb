SUMMARY = "CLI for extracting streams from various websites to a video player of your choosing"
DESCRIPTION = "Streamlink is a command-line utility that pipes video streams from various services into a video player, such as VLC."
HOMEPAGE = "https://github.com/streamlink/streamlink"
SECTION = "devel/python"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=15519b204ac11ccc2e4c72b87d310191"

DEPENDS += "${PYTHON_PN}-versioningit-native"

RDEPENDS:${PN} = "${PYTHON_PN}-core \
	${PYTHON_PN}-ctypes \
	${PYTHON_PN}-futures3 \
	${PYTHON_PN}-isodate \
	${PYTHON_PN}-pycountry \
	${PYTHON_PN}-lxml \
	${PYTHON_PN}-misc \
	${PYTHON_PN}-pkgutil \
	${PYTHON_PN}-pycryptodome \
	${PYTHON_PN}-pysocks \
	${PYTHON_PN}-requests \
	${PYTHON_PN}-shell \
	${PYTHON_PN}-singledispatch \
	${PYTHON_PN}-websocket-client \
"

inherit setuptools3 python3-dir gittag python3-compileall

PV = "git${SRCPV}"
PKGV = "${GITPKGVTAG}"

SRCREV_plugins = "${AUTOREV}"
SRCREV_FORMAT = "streamlink_plugins"

SRC_URI = " git://github.com/streamlink/streamlink;protocol=https;branch=master \
			git://github.com/oe-mirrors/streamlink-plugins;protocol=https;branch=master;name=plugins;destsuffix=additional-plugins \
			file://hardcoded-version.patch \
			file://remove-exceptiongroup-import.patch \
"

S = "${WORKDIR}/git"

do_unpack:append() {
    bb.build.exec_func('do_prepare_plugins_dir', d)
}

do_prepare_plugins_dir() {
	cp -f ${WORKDIR}/additional-plugins/*.py ${S}/src/streamlink/plugins
}

do_install:append() {
    rm -rf ${D}${bindir}
    rm -rf ${D}${PYTHON_SITEPACKAGES_DIR}/streamlink_cli
    rm -rf ${D}${PYTHON_SITEPACKAGES_DIR}/*.egg-info
    rm -rf ${D}${PYTHON_SITEPACKAGES_DIR}/streamlink/plugins/.removed
    rm -rf ${D}${PYTHON_SITEPACKAGES_DIR}/*dirty.dist-info
    rm -rf ${D}${datadir}
    cp -r ${S}/src/streamlink ${D}${PYTHON_SITEPACKAGES_DIR}/
}

include ${PYTHON_PN}-package-split.inc

PACKAGES = "${PN}"

FILES:${PN} += " \
    ${PYTHON_SITEPACKAGES_DIR}/streamlink/*.pyc \
    ${PYTHON_SITEPACKAGES_DIR}/streamlink/*/*.pyc \
    ${PYTHON_SITEPACKAGES_DIR}/streamlink/*/*/*.pyc \
    "

FILES:${PN}-src += " \
    ${PYTHON_SITEPACKAGES_DIR}/streamlink-*.egg-info/* \
    ${PYTHON_SITEPACKAGES_DIR}/streamlink/plugins/.removed \
    ${PYTHON_SITEPACKAGES_DIR}/streamlink/*.py \
    ${PYTHON_SITEPACKAGES_DIR}/streamlink/*/*.py \
    ${PYTHON_SITEPACKAGES_DIR}/streamlink/*/*/*.py \
    "
