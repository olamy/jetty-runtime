/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2015 Dandelion
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.core.asset.generator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Abstract asset generator that 
 * </p>
 * 
 * @author Romain Lespinasse
 * @author Thibault Duchateau
 * @since 1.0.0
 */
/**
 * 
 * @author Thibault Duchateau
 * 
 * @param <P>
 *           the type of the placeholder.
 * @param <C>
 *           the type of content associated with a placeholder.
 */
public abstract class AbstractAssetPlaceholderContentGenerator<P extends AssetPlaceholder, C extends AbstractAssetPlaceholderContent<P>>
      implements AssetContentGenerator {

   private static final Logger logger = LoggerFactory.getLogger(AbstractAssetPlaceholderContentGenerator.class);

   private final C content;

   protected AbstractAssetPlaceholderContentGenerator(C content) {
      this.content = content;
   }

   @Override
   public String getAssetContent(HttpServletRequest request) {
      logger.debug("Generating asset...");
      String generatedContent = getPlaceholderContent(request, content.getPlaceholderContent());
      logger.debug("Asset generated successfully");
      return generatedContent;
   }

   protected C getAssetContent() {
      return content;
   }

   protected abstract String getPlaceholderContent(HttpServletRequest request, Map<P, StringBuilder> contents);
}